package com.company;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Main {

    public static final Object Null = new NoArgument();
    public static String  _csvFilename = "";

    /**
     @aribaapi private
     */
    public static final Object[] NoMore = new Object[0];


    public static void main(String[] args) {
        Main test = new Main();
        _csvFilename = "PL-7006-S4_CreateGroup.csv";
	 System.out.println(" hi i am here :" + test.addTestInfoToUrl("https://buildbox285.ariba.com:7443/Sourcing/Main?passwordadapter=PasswordAdapter1&realm=s4All"));
    }


    public String getTestId ()
    {
        String testId = null;
        if (_csvFilename != null) {
            testId = _csvFilename.replaceAll("\\\\", "/");
            //strip out .csv extension
            if (testId.endsWith(".csv")) {
                testId = testId.substring(0, testId.length() - 4);
            }
            int index = testId.lastIndexOf('/');
            if (index > 0) {
                String filePath = testId.substring(0, index);
                String fileName = testId.substring(index+1, testId.length());
                // Replaces path separator by suite separtor . in the suite name
                filePath = filePath.replaceAll("/", ".");
                // replaces all . in the filename name by _
                // so that there is no confusion withsuite separator
                testId = S("%s#%s", filePath, fileName);
            }
        }
        return testId;
    }

    public static String S (String control, Object a1, Object a2)
    {
        // Optimize a common, simple case, even though people should know
        // to call concat for this.
        if (control == "%s%s" &&
                a1 instanceof String &&
                a2 instanceof String)
        {
            return ((String)a1).concat((String)a2);
        }
        return control.join("+",a1.toString(),a2.toString());
    }


    public static String getTestShortIdFromId (String testId)
    {
        // short testid is always generated replacing # by .
        testId = formatTestId(testId);
        if (!nullOrEmptyString(testId)) {
            return getMessageDigest(testId.getBytes(), "SHA-256");
        }
        return null;
    }

    public static String formatTestId(String testId) {
        if (!nullOrEmptyString(testId)) {
            testId = testId.replaceAll("#","\\.");
        }
        return testId;
    }

    public static boolean nullOrEmptyString (String string)
    {
        return (string == null) || (string.length() == 0);
    }

    public static String getMessageDigest (byte[] bytes, String algorithm)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(bytes);
            byte[] b = md.digest();
            String result = "";
            for (int i=0; i < b.length; i++) {
                result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
            }
            return result;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new SecurityException(e);
        }
    }

    private String addTestInfoToUrl (String url)
    {
        String separator = "?";
        String pattern = "%s%stestId=%s&testShortId=%s&testLineNb=%s";
        if (url.lastIndexOf('?') > 0) {
            if (!url.endsWith("&")) {
                separator = "&";
            }
            else {
                separator = "";
            }
        }
        try {
            StringBuffer sb = new StringBuffer();
            Formatter fmt = new Formatter(sb);
            fmt.format(pattern, url,separator,URLEncoder.encode(getTestId(), "UTF-8"),URLEncoder.encode(getShortTestId(), "UTF-8") ,"no");
            url = sb.toString();
        }
        catch (UnsupportedEncodingException e) {
            System.out.println("Cannot add test information to URL." + e);
        }
        return url;
    }

    private String getShortTestId ()
    {
        return getTestShortIdFromId(getTestId());
    }



}

class NoArgument
{
    public String toString ()
    {
        return "(missing argument)";
    }
}
