package com.microsoft.azure.documentdb.sample.esb;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.sample.dao.TodoDaoFactory;
import com.microsoft.azure.documentdb.sample.model.TodoItem;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveSubscriptionMessageResult;

/**
 * Created by vazvadsk on 2016-10-18.
 */
public class TopicHelper {
    private static final Gson gson = new Gson();
    public void processToDo(){
        Configuration config =
                ServiceBusConfiguration.configureWithSASAuthentication(
                        // TODO: provide valid Service Bus name
                        "<service-bus-name>",
                        "RootManageSharedAccessKey",
                        // TODO: provide valid KEY for Service Bus
                        "<service-bus-key>",
                        ".servicebus.windows.net"
                );

        ServiceBusContract service = ServiceBusService.create(config);

        try
        {
            ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
            opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

            while(true)  {
                ReceiveSubscriptionMessageResult resultSubMsg =
                        service.receiveSubscriptionMessage(
                                // TODO: provide valid Topic name
                                "valdatopic1",
                                // TODO: provide valid subscription name
                                "all",
                                opts);
                BrokeredMessage message = resultSubMsg.getValue();
                if (message != null && message.getMessageId() != null)
                {
                    System.out.println("MessageID: " + message.getMessageId());
                    // Display the topic message.
                    System.out.print("From topic: ");
                    byte[] b = new byte[200];
                    String s = "";
                    int numRead = message.getBody().read(b);
                    while (-1 != numRead)
                    {
                        String _s = new String(b);
                        s += _s.trim();
                        numRead = message.getBody().read(b);
                    }
                    System.out.print(s);
                    TodoItem itm = gson.fromJson(s, TodoItem.class);
                    TodoDaoFactory.getDao().updateTodoItem(itm.getId(), true);
                    System.out.println();
                    System.out.println("Custom Property [category]: " +
                            message.getProperty("Category"));
                    // Delete message.
                    System.out.println("Deleting this message.");
                    service.deleteMessage(message);
                }
                else
                {
                    System.out.println("Finishing up - no more messages.");
                    // break;
                    Thread.sleep(1000);
                    // Added to handle no more messages.
                    // Could instead wait for more messages to be added.
                }
            }
        }
        catch (ServiceException e) {
            System.out.print("ServiceException encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        catch (Exception e) {
            System.out.print("Generic exception encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
