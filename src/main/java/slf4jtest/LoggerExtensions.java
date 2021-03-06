package slf4jtest;

import java.util.Collection;
import java.util.regex.Pattern;

interface LoggerExtensions {

    /* access to the underlying detailed LogMessage objects */
    Collection<LogMessage> lines();

    /* verify that a regex matches the logging of some log level level*/
    boolean matches(Predicate<LogMessage> regex);

    /* verify that a regex matches the logging of some log level level.
    * matches using Pattern.DOTALL
    */
    boolean matches(String regex);

    /* verify that a regex matches the logging of some log level level*/
    boolean matches(Pattern regex);

    /* verify that a regex matches the logging for a specific log level
    * matches using Pattern.DOTALL
    * */
    boolean matches(LogLevel level, String regex);

    /* verify that a regex matches the logging for a specific log level */
    boolean matches(LogLevel level, Pattern regex);

    /* verify that a predicate matches */
    boolean assertMatches(Predicate<LogMessage> predicate) throws Error;

    /* verify that the logging for some log level contains the substring.
     * does a String.contains(String) style comparison
     */
    boolean contains(String substring);

    /* verify that the logging for a specific log level contains the substring.
     * does a String.contains(String) style comparison
     */
    boolean contains(LogLevel level, String substring);

    /* erase the captured logging */
    void clear();
}
