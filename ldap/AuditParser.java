package ldap;

import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import jdk.internal.ref.Cleaner;
import  sun.nio.ch.DirectBuffer ;


public class AuditParser {

    //private static final long MAP_SIZE = 100 * 1024 * 1024; // 100 MB in bytes

    private static final long MAP_SIZE = 100 * 1024 * 1024; // 100 MB in bytes

    public static void main(String[] args) throws java.io.IOException {

        if (args.length != 2) {
            System.out.println("audit file to parse.... ");
            System.out.println("Utilisation : java AuditParser <audit.log> <String delimiter>");
            System.exit(1);
        }

        String logFilePath = args[0];
        Path pathToRead = Paths.get(logFilePath);

        if (!Files.exists(pathToRead) || !Files.isRegularFile(pathToRead)) {
            System.out.println("Le fichier " + logFilePath + " n'existe pas ou n'est pas un fichier régulier.");
            System.exit(1);
        }

        String delimiter = args[1];
        final byte[] delimiter_byte = delimiter.getBytes(StandardCharsets.UTF_8);
        long count = 0;
        long position = 0;
        System.out.println("delimiter_byte: size: " + delimiter_byte.length);

        CharBuffer charBuffer = null;

        FileChannel fileChannel = (FileChannel) Files.newByteChannel(pathToRead, EnumSet.of(StandardOpenOption.READ));

        long file_length = fileChannel.size();
        System.out.println("fileChannel: size: " + fileChannel.size());
        System.out.println("fileChannel: isOpen: " + fileChannel.isOpen());
        System.out.println("fileChannel: position: " + fileChannel.position());


        /*
        MappedByteBuffer mappedByteBuffer = fileChannel
                .map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());


        System.out.println("mappedByteBuffer: isLoaded: " + mappedByteBuffer.isLoaded());  //prints false
        System.out.println("mappedByteBuffer capacity: " + mappedByteBuffer.capacity());  //Get the size based on content size of file
        System.out.println("mappedByteBuffer isDirect: " + mappedByteBuffer.isDirect());
        System.out.println("mappedByteBuffer isReadOnly: " + mappedByteBuffer.isReadOnly());
        System.out.println("mappedByteBuffer limit: " + mappedByteBuffer.limit());

        System.out.println("mappedByteBuffer hasArray: " + mappedByteBuffer.hasArray());
        System.out.println("mappedByteBuffer hasRemain :" + mappedByteBuffer.hasRemaining());
        System.out.println("mappedByteBuffer position: " + mappedByteBuffer.position());

        mappedByteBuffer.load();
        mappedByteBuffer.hasArray();
        try {
            Thread.sleep(150);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

*/
        byte[] tab = new byte[200];
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

        /*
        System.out.println("mappedByteBuffer hasRemain :" + mappedByteBuffer.hasRemaining());
        System.out.println("mappedByteBuffer position: " + mappedByteBuffer.position());
        //mappedByteBuffer.get(tab) ;
        System.out.println("mappedByteBuffer hasRemain :" + mappedByteBuffer.hasRemaining());
        System.out.println("mappedByteBuffer position: " + mappedByteBuffer.position());
*/



        long length = fileChannel.size();
        int nbr_mapped = 0;
        while (position < length) {

            long remaining = length - position;
            long bytestomap = (long) Math.min(MAP_SIZE, remaining);
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, bytestomap);
            mappedByteBuffer.load() ;
            mappedByteBuffer.hasArray() ;
            mappedByteBuffer.limit() ;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            nbr_mapped ++ ;

            System.out.println("mappedByteBuffer: isLoaded: " + mappedByteBuffer.isLoaded());  //prints false
            System.out.println("mappedByteBuffer capacity: " + mappedByteBuffer.capacity());  //Get the size based on content size of file
            System.out.println("mappedByteBuffer isDirect: " + mappedByteBuffer.isDirect());
            System.out.println("mappedByteBuffer isReadOnly: " + mappedByteBuffer.isReadOnly());
            System.out.println("mappedByteBuffer limit: " + mappedByteBuffer.limit());

            long limit = mappedByteBuffer.limit();


            long lastSpace = -1;
            long firstChar = -1;
            while (mappedByteBuffer.hasRemaining()) {
                boolean isFirstChar = false;
                while (firstChar != 0 && mappedByteBuffer.hasRemaining()) {
                    byte currentByte = mappedByteBuffer.get();
                    if (delimiter_byte[0] == currentByte) {
                        isFirstChar = true;
                        break;
                    }
                }
                if (isFirstChar) {
                    boolean isRestOfChars = true;
                    int j;
                    for (j = 1; j < delimiter_byte.length; j++) {
                        if (!mappedByteBuffer.hasRemaining() || delimiter_byte[j] != mappedByteBuffer.get()) {
                            isRestOfChars = false;
                            break;
                        }
                    }
                    if (isRestOfChars) {
                        count++;
                        lastSpace = -1;
                    }
                    firstChar = -1;
                }
            }


            if (lastSpace > -1) {
                position = position - (limit - lastSpace);
            }

            position += bytestomap;

/*
            Cleaner cleaner = ((DirectBuffer) mappedByteBuffer).cleaner();
            if (cleaner != null) {
                cleaner.clean();
            }
*/

        }


        System.out.println("done reading file: " + count);
        System.out.println("nbr_mapped: " + nbr_mapped);


        try {
            Thread.sleep(15000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        fileChannel.close();


    }


}
