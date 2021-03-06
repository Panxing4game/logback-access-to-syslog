package ch.qos.logback.access.net;

import ch.qos.logback.access.PatternLayout;
import ch.qos.logback.access.pattern.SyslogStartConverter;
import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.net.SyslogAppenderBase;
import ch.qos.logback.core.net.SyslogOutputStream;

import java.net.SocketException;
import java.net.UnknownHostException;


public class SyslogAppender extends SyslogAppenderBase<IAccessEvent> {

  // Default 'common' pattern
  private static final String DEFAULT_SUFFIX_PATTERN = "%h %l %u %user %date \"%r\" %s %b";

  @Override
  public Layout<IAccessEvent> buildLayout() {
    PatternLayout layout = new PatternLayout();
    layout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());
    if (suffixPattern == null) {
      suffixPattern = DEFAULT_SUFFIX_PATTERN;
    }
    layout.setPattern(getPrefixPattern() + suffixPattern);
    layout.setContext(getContext());
    layout.start();
    return layout;
  }

  @Override
  public SyslogOutputStream createOutputStream() throws UnknownHostException, SocketException {
    return new SyslogOutputStream(getSyslogHost(), getPort());
  }

  @Override
  public int getSeverityForEvent(Object eventObject) {
    return SyslogStartConverter.SYSLOG_ACCESS_SEVERITY;
  }

  private String getPrefixPattern() {
    return "%syslogStart{" + getFacility() + "}";
  }
}
