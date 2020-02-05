package com.telek.hemsipc.protocal3761.refresh;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HotFileListener extends FileAlterationListenerAdaptor {

    private static Logger log = LoggerFactory.getLogger(HotFileListener.class);

    /**
     * @param
     * @return
     * @description 启动监听
     * @version 2.0, 2019/1/24 15:08
     * @author <a href="mailto:Tastill">Tastill</a>
     */
    @Override
    public void onStart(FileAlterationObserver observer) {
        log.info("启动监听器：");
    }

    /**
     * @param
     * @return
     * @description 文件创建
     * @version 2.0, 2019/1/24 14:59
     * @author <a href="mailto:Tastill">Tastill</a>
     */
    @Override
    public void onFileCreate(File file) {
        getRefresh(file);
    }

    private void getRefresh(File file) {
        String name = file.getName();
        if (Afn4F10HotRefresh.FILE_NAME.equals(name)) {
            log.info("set afn4 f10");
            Afn4F10HotRefresh.refresh();
        } else if (Afn4F7HotRefresh.FILE_NAME.equals(name)) {
            log.info("set afn4 f7 终端ip设置");
            Afn4F7HotRefresh.refresh();
        } else if (GetF7HotRefresh.FILE_NAME.equals(name)) {
            log.info("get f7 终端ip设置");
            GetF7HotRefresh.refresh();
        }
    }

    /**
     * @param
     * @return
     * @description 文件内容发生变化
     * @version 2.0, 2019/1/24 15:05
     * @author <a href="mailto:Tastill">Tastill</a>
     */
    @Override
    public void onFileChange(File file) {
        getRefresh(file);
    }

    /**
     * @param
     * @return
     * @description 监听停止
     * @version 2.0, 2019/1/24 15:07
     * @author <a href="mailto:Tastill">Tastill</a>
     */
    @Override
    public void onStop(FileAlterationObserver observer) {
        log.info("监听停止");
    }
}
