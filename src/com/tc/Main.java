package com.tc;

public class Main {

    public static void main(String[] args) {
        // write your code here
        if (args.length == 0)
        {
            System.err.printf("*** Usage: FindIpHost.jar  fileName  ***");
            System.exit(1);
        }

        String fileName=args[0];//"/home/wu/Desktop/ip_host.cvs";
        CSVFileUtil csvFileUtil=new CSVFileUtil();
        csvFileUtil.handleCVSFile(fileName);
    }
}
