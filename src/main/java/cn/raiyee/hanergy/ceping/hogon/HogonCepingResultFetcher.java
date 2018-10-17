package cn.raiyee.hanergy.ceping.hogon;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/*
First of all, you can't mark a message as read if you are using a POP3 server - the POP3 protocol doesn't support that.
 */
public class HogonCepingResultFetcher {
    @SneakyThrows
    public static void fetchMails(String host, String port, String userName, String password) {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", port);
        Session session = Session.getDefaultInstance(properties);

        // connects to the message store
        @Cleanup Store store = session.getStore("pop3");
        store.connect(userName, password);

        // opens the inbox folder
        @Cleanup Folder folderInbox = store.getFolder("INBOX");
        folderInbox.open(Folder.READ_ONLY);

        // fetches new messages from server
        int messageCount = folderInbox.getMessageCount();
        Message[] arrayMessages = folderInbox.getMessages(1, messageCount);

        for (int i = 0; i < arrayMessages.length; i++) {
            Message message = arrayMessages[i];

            System.out.println("Message #" + (i + 1) + ":");
            System.out.println("\t From: " + ((InternetAddress) message.getFrom()[0]).getAddress());
            System.out.println("\t Subject: " + message.getSubject());
            System.out.println("\t Sent Date: " + new DateTime(message.getSentDate()).toString("yyyy-MM-dd HH:mm:ss.SSS"));
            System.out.println("\t ContentType: " + message.getContentType());
            System.out.println("\t Message: " + getText(message));

            if (message.getContent() instanceof Multipart) {
                Multipart multi = (Multipart) message.getContent();
                for (int j = 0; j < multi.getCount(); j++) {
                    BodyPart bodyPart = multi.getBodyPart(j);
                    if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
                            StringUtils.isBlank(bodyPart.getFileName())) {
                        continue; // dealing with attachments only
                    }
                    InputStream is = bodyPart.getInputStream();
                    String fileName = MimeUtility.decodeText(bodyPart.getFileName());
                    System.out.println("\t Attachment: " + fileName);
                    File f = new File("./" + fileName);

                    Files.copy(is, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public static void main(String[] args) {

    }


    /**
     * Return the primary text content of the message.
     */
    @SneakyThrows
    private static String getText(Part p) {
        if (p.isMimeType("text/*")) {
            return (String) p.getContent();
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) return s;
            }
        }

        return null;
    }
}
