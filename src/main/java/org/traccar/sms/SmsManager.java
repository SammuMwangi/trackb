
package org.traccar.sms;

import org.traccar.notification.MessageException;

public interface SmsManager {

    void sendMessage(String destAddress, String message, boolean command) throws MessageException;

}
