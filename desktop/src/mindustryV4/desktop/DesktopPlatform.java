package mindustryV4.desktop;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import mindustryV4.Vars;
import mindustryV4.core.Platform;
import mindustryV4.ui.dialogs.FileChooser;
import ucore.function.Consumer;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class DesktopPlatform extends Platform{
    final static String applicationId = "398246104468291591";
    String[] args;

    public DesktopPlatform(String[] args){
        this.args = args;

        Vars.testMobile = Array.with(args).contains("-testMobile", false);
    }

    @Override
    public void showFileChooser(String text, String content, Consumer<FileHandle> cons, boolean open, String filter){
        new FileChooser(text, file -> file.extension().equalsIgnoreCase(filter), open, cons).show();
    }

    @Override
    public String getUUID(){
        try{
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            NetworkInterface out;
            for(out = e.nextElement(); (out.getHardwareAddress() == null || !validAddress(out.getHardwareAddress())) && e.hasMoreElements(); out = e.nextElement()) ;

            byte[] bytes = out.getHardwareAddress();
            byte[] result = new byte[8];
            System.arraycopy(bytes, 0, result, 0, bytes.length);

            String str = new String(Base64Coder.encode(result));

            if(str.equals("AAAAAAAAAOA=")) throw new RuntimeException("Bad UUID.");

            return str;
        }catch(Exception e){
            return super.getUUID();
        }
    }

    private boolean validAddress(byte[] bytes){
        if(bytes == null) return false;
        byte[] result = new byte[8];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        return !new String(Base64Coder.encode(result)).equals("AAAAAAAAAOA=");
    }
}
