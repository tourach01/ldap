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


        System.out.println("fileChannel: size: " + fileChannel.size());
        System.out.println("fileChannel: isOpen: " + fileChannel.isOpen());
        System.out.println("fileChannel: position: " + fileChannel.position());


        MappedByteBuffer mappedByteBuffer = fileChannel
                .map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());


        System.out.println("mappedByteBuffer: isLoaded: " + mappedByteBuffer.isLoaded());  //prints false
        System.out.println("mappedByteBuffer capacity: " + mappedByteBuffer.capacity());  //Get the size based on content size of file
        System.out.println("mappedByteBuffer isDirect: " + mappedByteBuffer.isDirect());
        System.out.println("mappedByteBuffer isReadOnly: " + mappedByteBuffer.isReadOnly());
        System.out.println("mappedByteBuffer limit: " + mappedByteBuffer.limit());

        System.out.println("mappedByteBuffer hasArray: " + mappedByteBuffer.hasArray() );
        System.out.println("mappedByteBuffer hasRemain :" +mappedByteBuffer.hasRemaining());
        System.out.println("mappedByteBuffer position: "+ mappedByteBuffer.position());

        mappedByteBuffer.load() ;
        mappedByteBuffer.hasArray() ;
        try {
            Thread.sleep(150);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }


        byte[] tab=  new byte[200];
        System.out.println("tab.length: " + tab.length);

/*

        if (mappedByteBuffer != null) {
            charBuffer = Charset.forName("UTF-8").decode(mappedByteBuffer);
            System.out.println("charBuffer hasArray: "+ charBuffer.hasArray() );
            System.out.println("charBuffer isReadOnly: "+ charBuffer.isReadOnly());
            System.out.println("charBuffer length: " + charBuffer.length() );

            char[] tab_cb =  charBuffer.array() ;
            System.out.println("tab_cb length: " + tab_cb.length);

        }
*/
        System.out.println("mappedByteBuffer hasRemain :" +mappedByteBuffer.hasRemaining());
        System.out.println("mappedByteBuffer position: "+ mappedByteBuffer.position());
        mappedByteBuffer.get(tab) ;
        System.out.println("mappedByteBuffer hasRemain :" +mappedByteBuffer.hasRemaining());
        System.out.println("mappedByteBuffer position: "+ mappedByteBuffer.position());




        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        fileChannel.close();


    }


}
