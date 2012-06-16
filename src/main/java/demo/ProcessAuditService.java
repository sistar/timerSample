package demo;

/**
 * Created with IntelliJ IDEA.
 * User: RSI
 * Date: 16.06.12
 * Time: 20:34
 * To change this template use File | Settings | File Templates.
 */
public interface ProcessAuditService {
    void audit(String businessState, Integer taskState) throws Exception;
}
