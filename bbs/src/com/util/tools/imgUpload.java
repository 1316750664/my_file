package com.util.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xia on 2014/10/27.
 */
public class imgUpload {

    /*
     * 拷贝前的判断 f1该文件通过文件系统中的路径名 f2新的文件通过文件系统中的路径名 fs是新的文件名称
     */
    public void bcopy(File f1, File f2) {
        if (f1.exists()) {
            // 判断要拷贝的文件是否存在
            if (!f2.exists()) {//判f2是否存在
                f2.mkdirs();// 建立相应的目录
            }
            copy(f1, f2);//调用copy方法
        }
    }

    /**
     * @param args 文件字节输入、输出流方法拷贝一个文件到指定目录
     */
    private void copy(File f1, File f2) {//f1 源文件路径  f2目标路径   最后把f2 insert 到数据库就不写了
        try {
            // 建立相关的字节输入流
            FileInputStream fr = new FileInputStream(f1); // 通过打开一个到实际文件的连接来创建一个
            // FileInputStream，该文件通过文件系统中的路径名
            // 创建一个向具有指定名称的文件中写入数据的输出文件流。
            FileOutputStream fw = new FileOutputStream(f2);
            byte buffer[] = new byte[1]; // 声明一个byte型的数组，数组的大小是512个字节
            while (fr.read(buffer) != -1) { // read()从此输入流中读取一个数据字节，只要读取的结果不！=-1就执行while循环中的语句块
                fw.write(buffer); //write(byte[] b)将 b.length 个字节从指定字节数组写入此文件输出流中。
            }
            fw.close();// 关闭此文件输出流并释放与此流有关的所有系统资源。
            fr.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
