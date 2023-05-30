package io;

import control.WindowController;

import java.beans.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FileIO {
    private String fileJBP = "file.xml";
    private WindowController windowController;
    public FileIO(WindowController windowController) {
        this.windowController = windowController;
    }

    FileOutputStream getOutputStream(String filename) throws IOException {
        File file = new File( filename );
        if (!file.exists())
            file.createNewFile();
        return new FileOutputStream( filename );
    }
    FileInputStream getInputStream(String filename) throws IOException {
        File file = new File( filename );
        if (!file.exists())
            throw new FileNotFoundException(filename + " does not exist!");
        return new FileInputStream( filename );
    }

    public void saveFileJBP(String fileName) {
        try {
            FileOutputStream out = getOutputStream(fileName);
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(out));
            encoder.setPersistenceDelegate(BigDecimal.class, encoder.getPersistenceDelegate(Integer.class));
            // Quelle: https://coderanch.com/t/750378/java/Serialize-object-LocalDate-XML
            // (for serializing/deserializing LocalDate in XML)
            encoder.setPersistenceDelegate(LocalDate.class, new PersistenceDelegate() {
                @Override
                protected boolean mutatesTo(Object oldInstance, Object newInstance) {
                    return oldInstance.equals(newInstance);
                }

                @Override
                protected Expression instantiate(Object oldInstance, Encoder out) {
                    return new Expression(oldInstance, LocalDate.class, "parse", new Object[] {
                            oldInstance.toString()
                    });
                }
            });
            encoder.writeObject(windowController);
            encoder.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WindowController loadFileJBP(String fileName) {
        try {
            FileInputStream in = getInputStream(fileName);
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
            this.windowController = (WindowController) decoder.readObject();
            decoder.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.windowController;
    }
}

