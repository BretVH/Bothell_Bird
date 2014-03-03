package bothellbird;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class FileMaker 
{
	private File outputBinaryFile2;

	public FileMaker() 
	{
	}
	public File make(int ID, char gender, int num, String type) 
	{
		try
		{
		Connection conn = SimpleDataSource.getconnection();
		Statement stat = null;
		stat = conn.createStatement();
		String FileName = ID + "" + gender + "" + num + "." +  type;
        String query = "SELECT * FROM" 
                + " BirdsDatabase.dbo.MyBirdStore Where name = '" 
        		+ FileName + "'";  //get database table
        ResultSet rs = null;
        rs = stat.executeQuery(query);
        rs.next();
        String outputBinaryFileName2 = FileName;
        outputBinaryFile2 = new File(outputBinaryFileName2);
        FileOutputStream  outputFileOutputStream = null;
        try {
			outputFileOutputStream  = new FileOutputStream(outputBinaryFile2);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        Blob image = rs.getBlob("file_stream");
        InputStream blobInputStream  = (InputStream) image.getBinaryStream();
        int                 bytesRead                   = 0;
        int                 bytesWritten                = 0;
        int                 totBytesRead                = 0;
        int                 totBytesWritten             = 0;

        int chunkSize = (int)image.length();
        byte[] binaryBuffer = new byte[chunkSize];
        
        try {
			while ((bytesRead = blobInputStream.read(binaryBuffer)) != -1) {
			    
			    // Loop through while reading a chunk of data from the BLOB
			    // column using an InputStream. This data will be stored
			    // in a temporary buffer that will be written to disk.
			    outputFileOutputStream.write(binaryBuffer, 0, bytesRead);
			    
			    totBytesRead += bytesRead;
			    totBytesWritten += bytesRead;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
			outputFileOutputStream.close();
			blobInputStream.close();
        
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
     conn.close();
		}
     catch (SQLException e){
    	 outputBinaryFile2 = null;
     }
     return outputBinaryFile2;
	}
}
