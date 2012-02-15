package uk.co.jordanmcqueen.Broadcast;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Broadcast extends JavaPlugin
{

    public Broadcast()
    {
        messages = new ArrayList();
        defaultTime = 10000;
        prefix = "";
        random = false;
        log = Logger.getLogger("Minecraft");
        NL = System.getProperty("line.separator");
        fname = (new StringBuilder("plugins")).append(File.separator).append("broadcast.properties").toString();
    }

    public String parseChat(String message)
    {
        String msg = message.replaceAll("\\$", "\247");
        return msg;
    }

    public void onEnable()
    {
        PluginDescriptionFile pdfFile = getDescription();
        try
        {
            loadData();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        if(random = true)
            Collections.shuffle(messages);
        Runnable r = new SendMessage(this);
        (new Thread(r)).start();
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion()+ " is enabled.");
    }

    public void onDisable()
    {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " is now disabled.");
    }

    public boolean isDebugging(Player player)
    {
        if(debugees.containsKey(player))
            return ((Boolean)debugees.get(player)).booleanValue();
        else
            return false;
    }

    @SuppressWarnings("unchecked")
	public void setDebugging(Player player, boolean value)
    {
        debugees.put(player, Boolean.valueOf(value));
    }

    @SuppressWarnings("unchecked")
	public void loadData()
        throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new FileInputStream(fname), "UTF-8");
        while(scanner.hasNextLine()) 
        {
            String split[] = scanner.nextLine().split("=");
            if(split[0].equalsIgnoreCase("msg"))
                messages.add(split[1]);
            else
            if(split[0].equalsIgnoreCase("time"))
                defaultTime = Integer.parseInt(split[1]) * 1000;
            else
            if(split[0].equalsIgnoreCase("prefix"))
                prefix = (new StringBuilder(String.valueOf(split[1]))).append(" ").toString();
            else
            if(split[0].equalsIgnoreCase("random"))
                random = Boolean.parseBoolean(split[1]);
        }
        return;
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String args[])
    {
        String commandName = command.getName().toLowerCase();
        if(commandName.equals("reloadmsg"))
        {
              if((sender instanceof Player) && sender.isOp())
            {
                messages.clear();
                prefix = "";
                try
                {
                    loadData();
                }
                catch(FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                if(random = true)
                    Collections.shuffle(messages);
                sender.sendMessage((new StringBuilder(String.valueOf(parseChat(prefix)))).append("Broadcast Reloaded").toString());
                return true;
            }
 if((sender instanceof Player) && sender.hasPermission("broadcast.reload"))
            {
                messages.clear();
                prefix = "";
                try
                {
                    loadData();
                }
                catch(FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                if(random = true)
                    Collections.shuffle(messages);
                sender.sendMessage((new StringBuilder(String.valueOf(parseChat(prefix)))).append("Broadcast Reloaded").toString());
                return true;
            }
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
	private final HashMap debugees = new HashMap();
    @SuppressWarnings("rawtypes")
	ArrayList messages;
    int defaultTime;
    String prefix;
    boolean random;
    Logger log;
    String NL;
    String fname;
}
