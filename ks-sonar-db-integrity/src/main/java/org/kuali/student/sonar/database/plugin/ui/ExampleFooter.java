package org.kuali.student.sonar.database.plugin.ui;

import org.sonar.api.web.Footer;

public final class ExampleFooter implements Footer {

  public String getHtml() {
    return "<p>Footer Example - <em>This is static HTML</em></p>";
  }
}
