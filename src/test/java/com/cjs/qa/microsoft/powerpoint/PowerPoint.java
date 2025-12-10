package com.cjs.qa.microsoft.powerpoint;

import com.cjs.qa.core.Environment;
import com.cjs.qa.utilities.IExtension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
//  import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public final class PowerPoint {
  private PowerPoint() {
    // Utility class - prevent instantiation
  }

  public static void main(String[] args) throws IOException {
    final String fileName = "createPPTX" + IExtension.PPTX;
    final String title = "Power Point Example";
    //
    File file = new File(fileName);
    try (XMLSlideShow xmlSlideShow = new XMLSlideShow();
        FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      xmlSlideShow.write(fileOutputStream);
      Environment.sysOut(fileName + " created successfully");
    }
    //
    file = new File(fileName);
    try (FileInputStream fileInputStream = new FileInputStream(file);
        XMLSlideShow xmlSlideShow = new XMLSlideShow(fileInputStream)) {
      //
      int iSlideMaster = 0;
      for (final XSLFSlideMaster xslfSlideMaster : xmlSlideShow.getSlideMasters()) {
        iSlideMaster++;
        int iSlideLayout = 0;
        for (final XSLFSlideLayout xslfSlideLayout : xslfSlideMaster.getSlideLayouts()) {
          iSlideLayout++;
          Environment.sysOut(
              "Slide Master ("
                  + iSlideMaster
                  + "), Slide Layout: ("
                  + iSlideLayout
                  + ") ["
                  + xslfSlideLayout.getType().toString()
                  + "]");
        }
      }
      //
      // adding slides to the slideshow
      xmlSlideShow.createSlide();
      xmlSlideShow.createSlide();
      // saving the changes
      try (FileOutputStream fileOutputStream2 = new FileOutputStream(file)) {
        xmlSlideShow.write(fileOutputStream2);
      }
      Environment.sysOut(fileName + " edited successfully");
    }
    //
    try (XMLSlideShow xmlSlideShow = new XMLSlideShow()) {
      // getting the slide master object
      final XSLFSlideMaster xslfSlideMaster = xmlSlideShow.getSlideMasters().get(0);
      // get the desired slide layout
      final XSLFSlideLayout titleLayout = xslfSlideMaster.getLayout(SlideLayout.TITLE);
      // creating a slide with title layout
      final XSLFSlide xslfSlide = xmlSlideShow.createSlide(titleLayout);
      // selecting the place holder in it
      final XSLFTextShape title1 = xslfSlide.getPlaceholder(0);
      // setting the title init
      title1.setText(title);
      // create a file object
      file = new File(fileName);
      try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
        // save the changes in a PPt document
        xmlSlideShow.write(fileOutputStream);
        Environment.sysOut("slide cretated successfully");
      }
    }
  }
}
