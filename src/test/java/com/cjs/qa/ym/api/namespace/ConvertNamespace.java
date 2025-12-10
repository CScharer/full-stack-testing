package com.cjs.qa.ym.api.namespace;

import com.cjs.qa.core.Environment;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.IExtension;
import com.cjs.qa.utilities.JavaHelpers;
import com.cjs.qa.ym.api.services.YMAPI;
import com.cjs.qa.ym.api.services.YMService;
import java.util.Map;

public class ConvertNamespace extends YMService {
  public Map<String, String> toEasternTime(String localTime, String localGmtBias) throws Throwable {
    // Converts the given local time to current Eastern Time, factoring in
    // adjustments for Daylight Savings Time.
    Environment.sysOut(
        JavaHelpers.getCurrentClassMethodName()
            + " - "
            + URL_YM_API_DOC
            + "Convert_ToEasternTime"
            + IExtension.HTM);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(SessionNamespace.getSessionID());
    stringBuilder.append(
        Constants.nlTab(1, 1)
            + YMAPI.LABEL_CALL_METHOD_PREFIX
            + Constants.QUOTE_DOUBLE
            + "Convert.ToEasternTime"
            + Constants.QUOTE_DOUBLE
            + ">");
    stringBuilder.append(Constants.nlTab(1, 2) + "<LocalTime>" + localTime + "</LocalTime>");
    stringBuilder.append(
        Constants.nlTab(1, 2) + "<LocalGmtBias>" + localGmtBias + "</LocalGmtBias>");
    stringBuilder.append(Constants.nlTab(1, 1) + YMAPI.LABEL_CALL_METHOD_SUFFIX);
    return getAPIXMLResponse("POST", stringBuilder.toString());
  }
}
