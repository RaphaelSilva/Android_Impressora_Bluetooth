package br.com.impressorabluetooth.core.modelos;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedTransferQueue;

import br.com.impressorabluetooth.core.dao.Configuracao;
import br.com.impressorabluetooth.core.interfaces.IModeloInterpretador;
import br.com.impressorabluetooth.core.modelos.commands.ACommand;
import br.com.impressorabluetooth.core.modelos.commands.Break;
import br.com.impressorabluetooth.core.modelos.commands.CMD;
import br.com.impressorabluetooth.core.modelos.commands.CharacterModification;
import br.com.impressorabluetooth.core.modelos.commands.Image;
import br.com.impressorabluetooth.core.modelos.commands.ItalicPrint;
import br.com.impressorabluetooth.core.modelos.commands.PrintFeedForward;
import br.com.impressorabluetooth.core.modelos.commands.Reset;
import br.com.impressorabluetooth.core.modelos.commands.Text;

/**
 * Created by Raphael on 05/02/2015.
 */
public abstract class Command {

    protected List<String> attrs;
    protected String _name_cmd;
    protected boolean valid;

    public Command(){
        ACommand an = this.getClass().getAnnotation(ACommand.class);
        this._name_cmd = an.name();
        attrs = new ArrayList<>();
        attrs.add(VALUE);
    }

    public void writeBuf(){

    }

    public boolean isValid() {
        return valid;
    }

    public String getName(){
        return _name_cmd;
    }

    public void buildAttrs(XmlPullParser parser){
        for(int i = 0; i < parser.getAttributeCount(); i++){
            String attrName  = parser.getAttributeName(i);
            String attrValue = parser.getAttributeValue(i);
            if(attrName.equals(VALUE)){
                buildValue(attrValue);
            }else{
                try {
                    Command cmd = findCommand(attrName);
                    if(cmd != null) {
                        cmd.buildValue(attrValue);
                    } else {
                        this.addAttribute(attrName, attrValue);
                    }
                } catch (IllegalAccessException e) {
                    Log.i(this.toString(), "IllegalAccessException " + e.getMessage());
                } catch (InstantiationException e) {
                    Log.i(this.toString(), "IllegalAccessException "+e.getMessage());
                }
            }
        }
    }

    public abstract void buildValue(String text);
    public abstract void addAttribute(String attrName, String attrValue);

    protected static final String VALUE = "value";

    protected static Queue<Byte> buffer;
    private static Stack<Command> cmds;
    private static List<ACommand> cmds_list;
    protected static IModeloInterpretador _modelo;

    public static void Initialize(Command cmd, IModeloInterpretador modelo){
        _modelo = modelo;
        cmds = new Stack<>();
        buffer = new LinkedList<>();

        cmds_list = new ArrayList<>();

        cmds_list.add(CMD.class.getAnnotation(ACommand.class));
        cmds_list.add(CharacterModification.class.getAnnotation(ACommand.class));
        cmds_list.add(Text.class.getAnnotation(ACommand.class));
        cmds_list.add(Break.class.getAnnotation(ACommand.class));
        cmds_list.add(ItalicPrint.class.getAnnotation(ACommand.class));
        cmds_list.add(PrintFeedForward.class.getAnnotation(ACommand.class));
        cmds_list.add(Image.class.getAnnotation(ACommand.class));
        cmds_list.add(Reset.class.getAnnotation(ACommand.class));

        cmd.buildValue("0");

    }

    protected static Command findCommand(String name) throws IllegalAccessException, InstantiationException{
        if(name.equals("img")){
            Log.i("STOP", "STOP NOW");
        }
        for(ACommand c : cmds_list){
            if(c.name().equalsIgnoreCase(name)){
                Command cmd = (Command) c.css().newInstance();
                return cmd;
            }
        }
        return null;
    }

    public static Command create(String name) throws IllegalAccessException, InstantiationException {
        Command cmd = null;
        for(ACommand c : cmds_list){
            if(c.name().equalsIgnoreCase(name)){
                cmd = (Command) c.css().newInstance();
                cmd.valid = true;
                break;
            }
        }
        cmds.add(cmd);
        return cmd;
    }

    public static byte[] copiar() {
        byte [] b = new byte[buffer.size()];
        for(int i = 0; i < b.length; i++){
            b[i] =  buffer.poll();
        }
        return b;
    }

    public static Command get() {
        Command cmd = cmds.peek();
        return cmd;
    }

    public static Command endTag() {
        Command cmd = cmds.pop();
        if (Configuracao.DEBUG) {
            String txt = new String();
            Object [] memo = buffer.toArray();
            for(int i = 0; i < buffer.size(); i++){
                txt += String.format("%02x",(byte) memo[i]) + " ";
                if(i%15 == 0){
                    txt += "\n";
                }
            }
            Log.i("SHOW_MAP", txt);
        }
        return cmd;
    }

}

