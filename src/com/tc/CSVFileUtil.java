package com.tc;

/**
 * Created by wu on 11/7/15.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


public class CSVFileUtil {

    public static final String ENCODE = "UTF-8";


    private String findOKLineIp(String strOneLine) throws Exception {

        if(strOneLine.contains("Okay")){
            String[] subStr=strOneLine.split("Okay");
            if(subStr.length>1){
                String strOKLine= subStr[1];
                //all space,tab==>space
                strOKLine=strOKLine.replace("\t"," ");

                String[] strIPArray=strOKLine.split(" +");
                String ip=strIPArray[strIPArray.length-1];
               // System.out.println("get ip:"+ip);
                return ip;
            }
        }else{
            //ignore
           // System.out.println("ignore line");
           // System.out.println(strOneLine);
        }

        return null;
    }

    private String getIPV4(String strOKLineIp) throws Exception {

        if(strOKLineIp!=null){
            if(strOKLineIp.contains(":")){
               // System.out.println("it's ipv6,we ignore:"+strOKLineIp);
            }else{
                return strOKLineIp;
            }
        }

        return null;
    }



    public ArrayList<String> handleCVSFile(String fileName){
        FileInputStream fis = null;
        InputStreamReader isw = null;
        BufferedReader br = null;

        ArrayList<String> ipList=new ArrayList<String>();

        try {
            fis = new FileInputStream(fileName);
            isw = new InputStreamReader(fis, ENCODE);
            br = new BufferedReader(isw);

            String strOneLine;
            while(null!=(strOneLine=br.readLine())){
                String ip=findOKLineIp(strOneLine);
                if(ip!=null){
                    String ipv4=getIPV4(ip);
                    if(ipv4!=null && !ipList.contains(ipv4)){
                        ipList.add(ip);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(isw!=null){
                try {
                    isw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        testIpGetPing(ipList);

        return ipList;
    }



    //---------
    //refs:https://github.com/jptx1234/SimplePing/
    private String testIpGetPing(ArrayList<String> ipList){

        ArrayList<HostMode> hostModeArrayList=new ArrayList<HostMode>();

        try {
            for (String ip : ipList) {
                //win?
                //Process p=Runtime.getRuntime().exec("ping "+string+" -n 1");
                Process p=Runtime.getRuntime().exec("ping -c 1 "+ip);
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                StringBuilder sb=new StringBuilder();
                while((line = reader.readLine()) != null){
                    sb.append(line);
                }
                p.getInputStream().close();
                String[] mes=sb.toString().split(" +");

                for (int i = 0; i < mes.length; i++) {
                    String strMes=mes[i];
                    strMes=mes[i].toUpperCase();
                    if (strMes.startsWith("TIME=")) {
                        String timeVal=strMes.substring(strMes.indexOf("=")+1);
                        System.out.println(ip+ "\t" + timeVal);
                        int msTimeVal=Integer.valueOf(timeVal);
                        HostMode hostMode=new HostMode();
                        hostMode.ip=ip;
                        hostMode.timeMs=msTimeVal;
                        hostModeArrayList.add(hostMode);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String bestIp=findFastHost(hostModeArrayList);
        System.out.println("best ip:" + bestIp);

        return bestIp;
    }

    private String findFastHost(ArrayList<HostMode> hostList){
        if(hostList!=null && hostList.size()>0){
            Collections.sort(hostList,new SortHostList());
            return hostList.get(0).ip;
        }

        return null;
    }

}