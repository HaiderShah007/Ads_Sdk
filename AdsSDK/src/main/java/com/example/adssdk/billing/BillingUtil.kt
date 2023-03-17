package com.example.adssdk.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.example.adssdk.constants.Constant.KEY_IN_APP
import com.example.adssdk.constants.Constant.isDebug


class BillingUtil(val activity: Activity, private val successPurchase: SuccessPurchase) {
    var isBillingReady = false

    companion object {
        private var TAG: String = "billingApp"
    }

    private var lifeTime: String = "aod_premium"
    private var arrayListInApp = ArrayList<ProductDetails>()

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            Log.d(TAG, "getOldPurchases: in Listener")
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(activity, purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                Log.d(TAG, "getOldPurchases: User Cancelled")
            } else {
                Log.d(TAG, "getOldPurchases: Other Error")
            }
        }

    private var billingClient = BillingClient.newBuilder(activity)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    init {
        lifeTime = if (isDebug()) "android.test.purchased" else lifeTime

    }

    private fun setupConnection() {
        if (!isBillingReady) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        isBillingReady = true
                        val productList =
                            listOf(
                                QueryProductDetailsParams.Product.newBuilder()
                                    .setProductId(lifeTime)
                                    .setProductType(BillingClient.ProductType.INAPP)
                                    .build()
                            )
                        val params =
                            QueryProductDetailsParams.newBuilder().setProductList(productList)
                        billingClient.queryProductDetailsAsync(params.build()) { _, productDetails ->
                            if (productDetails.isNotEmpty()) {
                                for (itemDetail in productDetails) {
                                    arrayListInApp.add(itemDetail)
                                }
                                val productDetailsParamsList =
                                    listOf(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                            .setProductDetails(productDetails[0])
                                            .build()
                                    )
                                val billingFlowParams =
                                    BillingFlowParams.newBuilder()
                                        .setProductDetailsParamsList(productDetailsParamsList)
                                        .build()


                                val reqCode =
                                    billingClient.launchBillingFlow(activity, billingFlowParams)
                                isBillingReady =
                                    reqCode.responseCode == BillingClient.BillingResponseCode.OK


                            }
                        }
                        Log.d(TAG, "onBillingServiceDisconnected: Setup Connection")
                        Log.d(
                            TAG,
                            "onBillingServiceDisconnected:responseCode-> ${billingResult.responseCode}"
                        )
                        Log.d(
                            TAG,
                            "onBillingServiceDisconnected:debugMessage-> ${billingResult.debugMessage}"
                        )
                    } else {
                        isBillingReady = false
                    }
                }

                override fun onBillingServiceDisconnected() {
                    Log.d(TAG, "onBillingServiceDisconnected: Setup Connection Failed")
                    isBillingReady = false
                }
            })
        }
    }

    fun purchase() {
        setupConnection()
    }

    private fun handlePurchase(context: Context, purchase: Purchase) {
        val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener {
            Log.d(TAG, "getOldPurchases: debugMessage  ${it.debugMessage}")
            Log.d(TAG, "getOldPurchases: responseCode  ${it.responseCode}")
        }
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

            for (item in purchase.products) {
                if (item == lifeTime) {
                    PurchasePrefs(context).putBoolean(KEY_IN_APP, true)
                    Log.d(TAG, "handlePurchase: premium  $item")

                }
            }

            successPurchase.onSuccessPurchase()
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(
                    acknowledgePurchaseParams,
                    acknowledgePurchaseResponseListener
                )
            }

        }
    }

    fun checkPurchased(context: Context): Boolean {
        return PurchasePrefs(context).getBoolean(KEY_IN_APP)
    }

    interface SuccessPurchase {
        fun onSuccessPurchase(){}
    }

}

