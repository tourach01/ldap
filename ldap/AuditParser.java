package ldap;

import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class AuditParser {
    public static void main(String[] args) throws java.io.IOException {

        if (args.length != 1) {
            System.out.println("audit file to parse.... ");
            System.out.println("Utilisation : java AuditParser <audit.log>");
            System.exit(1);
        }

        String logFilePath = args[0];
        Path pathToRead = Paths.get(logFilePath);

        if (!Files.exists(pathToRead) || !Files.isRegularFile(pathToRead)) {
            System.out.println("Le fichier " + logFilePath + " n'existe pas ou n'est pas un fichier régulier.");
            System.exit(1);
        }


        CharBuffer charBuffer = null;
        FileChannel fileChannel = null;


        fileChannel = (FileChannel) Files.newByteChannel(pathToRead, EnumSet.of(StandardOpenOption.READ));


        System.out.println("fileChannel: size" + fileChannel.size());
        System.out.println("fileChannel: isOpen" + fileChannel.isOpen());
        System.out.println("fileChannel: position" + fileChannel.position());


        MappedByteBuffer mappedByteBuffer = fileChannel
                .map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());


        System.out.println("mappedByteBuffer: loaded:" + mappedByteBuffer.isLoaded());  //prints false
        System.out.println("mappedByteBuffer cap:" + mappedByteBuffer.capacity());  //Get the size based on content size of file
        System.out.println("mappedByteBuffer direct:" + mappedByteBuffer.isDirect());
        System.out.println("mappedByteBuffer ro:" + mappedByteBuffer.isReadOnly());
        System.out.println("mappedByteBuffer limit:" + mappedByteBuffer.limit());

        if (mappedByteBuffer != null) {
            charBuffer = Charset.forName("UTF-8").decode(mappedByteBuffer);
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        fileChannel.close();


    }


}
