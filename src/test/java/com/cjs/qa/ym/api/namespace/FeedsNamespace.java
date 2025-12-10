package com.cjs.qa.ym.api.namespace;

import com.cjs.qa.core.Environment;
import com.cjs.qa.utilities.Constants;
import com.cjs.qa.utilities.IExtension;
import com.cjs.qa.utilities.JavaHelpers;
import com.cjs.qa.ym.api.services.YMAPI;
import com.cjs.qa.ym.api.services.YMService;
import java.util.Map;

public class FeedsNamespace extends YMService {
  public Map<String, String> feedGet(String feedID, String pageSize, String startRecord)
      throws Throwable {
    // Returns a RSS 2.0 community feed.
    Environment.sysOut(
        JavaHelpers.getCurrentClassMethodName()
            + " - "
            + URL_YM_API_DOC
            + "Feeds_Feed_Get"
            + IExtension.HTM);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(SessionNamespace.getSessionID());
    stringBuilder.append(
        Constants.nlTab(1, 1)
            + YMAPI.LABEL_CALL_METHOD_PREFIX
            + Constants.QUOTE_DOUBLE
            + "Feeds.Feed.Get"
            + Constants.QUOTE_DOUBLE
            + ">");
    stringBuilder.append(Constants.nlTab(1, 2) + "<FeedID>" + feedID + "</FeedID>");
    stringBuilder.append(Constants.nlTab(1, 2) + "<PageSize>" + pageSize + "</PageSize>");
    stringBuilder.append(Constants.nlTab(1, 2) + "<StartRecord>" + startRecord + "</StartRecord>");
    stringBuilder.append(Constants.nlTab(1, 1) + YMAPI.LABEL_CALL_METHOD_SUFFIX);
    return getAPIXMLResponse("POST", stringBuilder.toString());
  }

  public Map<String, String> get() throws Throwable {
    // Returns a list of RSS 2.0 community feeds.
    Environment.sysOut(
        JavaHelpers.getCurrentClassMethodName()
            + " - "
            + URL_YM_API_DOC
            + "Feeds_Get"
            + IExtension.HTM);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(SessionNamespace.getSessionID());
    stringBuilder.append(
        Constants.nlTab(1, 1)
            + YMAPI.LABEL_CALL_METHOD_PREFIX
            + Constants.QUOTE_DOUBLE
            + "Feeds.Get"
            + Constants.QUOTE_DOUBLE
            + ">");
    stringBuilder.append(Constants.nlTab(1, 1) + YMAPI.LABEL_CALL_METHOD_SUFFIX);
    return getAPIXMLResponse("POST", stringBuilder.toString());
  }
}
