package com.cjs.qa.utilities;

import com.cjs.qa.core.Environment;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLT {
  public void main(String[] args) throws IOException, URISyntaxException, TransformerException {
    // Placeholder main method - not currently used
  }

  private String fileInput = null;
  private String fileOutput = null;
  private String fileTransform = null;
  private Map<String, String> parameters = new HashMap<>();

  public XSLT(String fileInput, String fileTransform, String fileOutput) {
    this.fileInput = fileInput;
    this.fileTransform = fileTransform;
    this.fileOutput = fileOutput;
    parameters = new HashMap<>();
  }

  public void addParamter(String key, String value) {
    parameters.put(key, value);
  }

  public boolean transformXML() {
    try {
      final TransformerFactory transformerFactory = TransformerFactory.newInstance();
      final Source sourceTransform = new StreamSource(new File(fileTransform));
      final Transformer transformer = transformerFactory.newTransformer(sourceTransform);
      final Set<String> keys = parameters.keySet();
      for (final String key : keys) {
        transformer.setParameter(key, parameters.get(key));
      }
      final Source sourceOutput = new StreamSource(new File(fileInput));
      transformer.transform(sourceOutput, new StreamResult(new File(fileOutput)));
    } catch (final Exception e) {
      Environment.sysOut(e);
      return false;
    }
    return true;
  }
}
