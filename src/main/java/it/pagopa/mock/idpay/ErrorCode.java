package it.pagopa.mock.idpay;

public final class ErrorCode {

    public static final String MODULE_ID = "A00";

    /*
     * Error codes.
     */

    // validation errors
    public static final String ERROR_INITIATIVEID_MUST_NOT_BE_NULL 							                = MODULE_ID + "000001";
    public static final String ERROR_INITIATIVENAME_MUST_NOT_BE_NULL 							            = MODULE_ID + "000002";
    public static final String ERROR_ORGANIZATIONNAME_MUST_NOT_BE_NULL 							            = MODULE_ID + "000003";
    public static final String ERROR_MERCHANTID_MUST_NOT_BE_NULL 							                = MODULE_ID + "000004";
    public static final String ERROR_STATUS_MUST_NOT_BE_NULL 							                    = MODULE_ID + "000005";
    public static final String ERROR_STARTDATE_MUST_NOT_BE_NULL 							                = MODULE_ID + "000006";
    public static final String ERROR_ENDDATE_MUST_NOT_BE_NULL 							                    = MODULE_ID + "000007";
    public static final String ERROR_SERVICEID_MUST_NOT_BE_NULL 							                = MODULE_ID + "000008";
    public static final String ERROR_ENABLED_MUST_NOT_BE_NULL 							                    = MODULE_ID + "000009";
    public static final String ERROR_TRANSACTIONFINALSTATUS_MUST_NOT_BE_NULL 							    = MODULE_ID + "00000A";
    public static final String ERROR_RETRIESSTATUSCHANGES_MUST_NOT_BE_NULL 							        = MODULE_ID + "00000B";
    public static final String ERROR_KID_MUST_NOT_BE_NULL 							                        = MODULE_ID + "00000C";
    public static final String ERROR_ENCSESSIONKEY_MUST_NOT_BE_NULL 							            = MODULE_ID + "00000D";
    public static final String ERROR_AUTHCODEBLOCK_MUST_NOT_BE_NULL 							            = MODULE_ID + "00000E";


    // rest server validation errors
    public static final String INITIATIVE_MUST_NOT_BE_EMPTY 							                    = MODULE_ID + "000030";
    public static final String TRANSACTION_CREATION_REQUEST_MUST_NOT_BE_EMPTY 							    = MODULE_ID + "000031";
    public static final String IPZS_REQUEST_MUST_NOT_BE_EMPTY 							                    = MODULE_ID + "000032";


    //Initiative retrieval error
    public static final String INITIATIVES_NOT_FOUND_FOR_MERCHANT 							                = MODULE_ID + "000040";

    // db errors
    public static final String INITIATIVE_NOT_FOUND_IDPAY_DB_FOR_MERCHANT 	 							    = MODULE_ID + "000050";
    public static final String ERROR_STORING_DATA_IN_DB 									                = MODULE_ID + "000051";
    public static final String ERROR_RETRIEVING_DATA_FROM_DB	 							                = MODULE_ID + "000052";
    public static final String ERROR_TRANSACTION_NOT_FOUND_IDPAY_DB	 							            = MODULE_ID + "000053";
    public static final String ERROR_INITIATIVE_NOT_FOUND_IDPAY_DB	 							            = MODULE_ID + "000054";
    public static final String ERROR_UPDATING_DATA_IN_DB 									                = MODULE_ID + "000055";
    /*
     * Error descriptions
     */
    public static final String ERROR_CALLING_IDPAY_REST_SERVICES_DESCR = "Error calling IdPay rest service";
    public static final String INITIATIVES_NOT_FOUND_FOR_MERCHANT_DESCR = "Initiatives weren’t found for merchant";
    public static final String INITIATIVE_NOT_FOUND_IDPAY_DB_FOR_MERCHANT_DESCR = "Initiative isn’t found for merchant";
    public static final String ERROR_STORING_DATA_IN_DB_DESCR = "Error storing data in DB";
    public static final String ERROR_RETRIEVING_DATA_FROM_DB_DESCR = "Error retrieving data from DB";
    public static final String ERROR_TRANSACTION_NOT_FOUND_IDPAY_DB_DESCR = "Transaction not found on idpay DB";
    public static final String ERROR_INITIATIVE_NOT_FOUND_IDPAY_DB_DESCR = "Initiative not found on idpay DB";
    public static final String ERROR_UPDATING_DATA_IN_DB_DESCR = "Error updating data in DB";
    /*
     * Error messages
     */
    public static final String INITIATIVES_NOT_FOUND_FOR_MERCHANT_MSG = "[" + INITIATIVES_NOT_FOUND_FOR_MERCHANT + "] " + INITIATIVES_NOT_FOUND_FOR_MERCHANT_DESCR;
    public static final String INITIATIVE_NOT_FOUND_IDPAY_DB_FOR_MERCHANT_MSG = "[" + INITIATIVE_NOT_FOUND_IDPAY_DB_FOR_MERCHANT + "] " + INITIATIVE_NOT_FOUND_IDPAY_DB_FOR_MERCHANT_DESCR;
    public static final String ERROR_STORING_DATA_IN_DB_MSG = "[" + ERROR_STORING_DATA_IN_DB + "] " + ERROR_STORING_DATA_IN_DB_DESCR;
    public static final String ERROR_RETRIEVING_DATA_FROM_DB_MSG = "[" + ERROR_RETRIEVING_DATA_FROM_DB + "] " + ERROR_RETRIEVING_DATA_FROM_DB_DESCR;
    public static final String ERROR_TRANSACTION_NOT_FOUND_IDPAY_DB_MSG = "[" + ERROR_TRANSACTION_NOT_FOUND_IDPAY_DB + "] " + ERROR_TRANSACTION_NOT_FOUND_IDPAY_DB_DESCR;
    public static final String ERROR_INITIATIVE_NOT_FOUND_IDPAY_DB_MSG = "[" + ERROR_INITIATIVE_NOT_FOUND_IDPAY_DB + "] " + ERROR_INITIATIVE_NOT_FOUND_IDPAY_DB_DESCR;
    public static final String ERROR_UPDATING_DATA_IN_DB_MSG = "[" + ERROR_UPDATING_DATA_IN_DB + "] " + ERROR_UPDATING_DATA_IN_DB_DESCR;

    private ErrorCode() {
    }
}
