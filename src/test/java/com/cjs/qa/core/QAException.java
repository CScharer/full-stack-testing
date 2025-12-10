package com.cjs.qa.core;

import com.cjs.qa.utilities.JavaHelpers;

@SuppressWarnings("PMD.DoNotExtendJavaLangThrowable") // Custom exception design
public class QAException extends Throwable {
  public static final String ERROR = "***ERROR***:";
  private static final long serialVersionUID = 1L;
  private static String qaErrorMessage;

  public QAException(Throwable throwable) {
    throwable(throwable);
  }

  public QAException(String message) {
    QAException.qaErrorMessage = message; // NOPMD - Intentional static state for last error message
    Environment.sysOut(ERROR + JavaHelpers.getCallingMethodName() + ":" + message);
  }

  public QAException(String message, Throwable throwable) {
    Environment.sysOut(ERROR + JavaHelpers.getCallingMethodName() + ":" + message);
    QAException.qaErrorMessage = message; // NOPMD - Intentional static state for last error message
    throwable(throwable);
  }

  public void throwable(Throwable throwable) {
    throwable.printStackTrace();
  }

  public static String getQaErrorMessage() {
    return qaErrorMessage;
  }

  public void setQaErrorMessage(String qaErrorMessage) {
    QAException.qaErrorMessage = qaErrorMessage;
  }
}
