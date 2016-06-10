package com.metashift.crash

import com.metashift.context.FileableContext
import org.crsh.shell.Shell
/**
 * A delegating shell that has noise from the spring context
 * Created by navid on 2/22/15.
 */
class ShellWithNoise {

    private @Delegate Shell shiz
    private FileableContext fileableContext
    int idx = 0
    def metaDude = [0:'( ●_●)',1:'( ●_0)',2:'( ●_●)',3:'( 0_●)']

    ShellWithNoise(Shell sh,FileableContext fileableContext){
        this.shiz = sh;
        this.fileableContext = fileableContext
    }

String getWelcome(){
    def hostName;
    try {
        hostName = java.net.InetAddress.getLocalHost().getHostName();
    } catch (java.net.UnknownHostException ignore) {
        hostName = "localhost";
    }
    return """\
           _             _    _  __ _
 _ __  ___| |_ __ _   __| |_ (_)/ _| |_
| '  \\/ -_)  _/ _` | (_-< ' \\| |  _|  _|
|_|_|_\\___|\\__\\__,_| /__/_||_|_|_|  \\__|


Welcome to $hostName + !
It is ${new Date()} now
""";
}

    // TODO: Add below on error :)
    // ( ●_●)-((⌼===<((() ≍≍≍≍≍ ♒ ✺ ♒ ZAP!

    /**
     * Returns the shell prompt.
     *
     * @return the shell prompt
     */
    String getPrompt(){
        if(idx > 3){
            idx = 0
        }
        def ret = "wat:${fileableContext.currentDirectory().name} ${metaDude[idx]}->"
        idx++
        ret
    }

}
