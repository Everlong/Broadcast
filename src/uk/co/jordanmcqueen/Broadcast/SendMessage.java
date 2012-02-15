package uk.co.jordanmcqueen.Broadcast;

import java.util.logging.Logger;

public class SendMessage
    implements Runnable
{

    public SendMessage(Broadcast instance)
    {
        log = Logger.getLogger("Minecraft");
        plugin = instance;
    }

    public void run()
    {
        String currentMessage = "";
        int currentPos = 0;
        while(plugin.isEnabled()) 
        {
            if(plugin.messages.size() > 0)
            {
                if(currentPos < plugin.messages.size())
                {
                    currentMessage = (String)plugin.messages.get(currentPos);
                } else
                {
                    currentPos = 0;
                    currentMessage = (String)plugin.messages.get(currentPos);
                }
                String lines[] = currentMessage.split("\\$n");
                for(int i = 0; i < lines.length; i++)
                {
                    if(i == 0)
                        lines[i] = (new StringBuilder(String.valueOf(plugin.parseChat(plugin.prefix)))).append(plugin.parseChat(lines[i])).toString();
                    else
                        lines[i] = plugin.parseChat(lines[i]);
                    if(lines[i].equals(""))
                        lines[i] = " ";
                }

                for(int a = 0; a < lines.length; a++)
                    plugin.getServer().broadcastMessage(lines[a]);

                currentPos++;
            }
            try
            {
                Thread.sleep(plugin.defaultTime);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private final Broadcast plugin;
    public String target;
    Logger log;
}
