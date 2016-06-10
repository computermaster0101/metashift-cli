import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.OnConsoleStatusListener

// always a good idea to add an on console status listener
statusListener(OnConsoleStatusListener)

def appenderList = ["ROLLING"]
def WEBAPP_DIR = "."
def consoleAppender = true;

def PROFILE = System.getProperty("spring.profiles.active");
if (PROFILE == "production") {
    WEBAPP_DIR = "~/metashift"
    consoleAppender = false
} else {
    appenderList.add("CONSOLE")
}

if (consoleAppender) {
    appender("CONSOLE", ConsoleAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
        }
    }
}

appender("ROLLING", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        Pattern = "%d %level %thread %mdc %logger - %m%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        FileNamePattern = "${WEBAPP_DIR}/log/metashift-%d{yyyy-MM}.zip"
    }
}

root(INFO, appenderList)

logger("org.apache.http.headers", INFO)
logger("org.apache.http.wire", INFO)
