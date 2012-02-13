package uk.co.jordanmcqueen.Broadcast;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Broadcast extends JavaPlugin {
	public static Broadcast plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public static int currentLine = 0;
	public static int tid = 0;
	public static int running = 1;
	public static long interval = 10;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is now disabled.");
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is now enabled.");
		
		tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				try {
					broadcastMessage("plugins/Broadcast/messages.txt");
				} catch (IOException e) {
					
				}
			}
				
		}, 0, interval * 20);
	}
	
	public static void broadcastMessage(String fileName) throws IOException {
		FileInputStream fs;
		fs = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		for(int i = 0; i < currentLine; ++i)
			br.readLine();
		String line = br.readLine();
		line = line.replaceAll("&f", ChatColor.WHITE + "");
		line = line.replaceAll("&e", ChatColor.YELLOW + "");
		line = line.replaceAll("&d", ChatColor.LIGHT_PURPLE + "");
		line = line.replaceAll("&a", ChatColor.GREEN + "");
		Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE +"[Broadcast] " + ChatColor.WHITE + line);
		LineNumberReader lnr = new LineNumberReader(new FileReader(new File(fileName)));
		lnr.skip(Long.MAX_VALUE);
		int lastLine = lnr.getLineNumber();
		if(currentLine + 1 == lastLine + 1) {
			currentLine = 0;
		} else {
			currentLine++;
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("stopbroadcast")) {
			if(running == 1) {
				Bukkit.getServer().getScheduler().cancelTask(tid);
				Player player = (Player) sender;
				player.sendMessage("Cancelled broadcasts.");
				running = 0;
			} else {
				Player player = (Player) sender;
				player.sendMessage("They aren't running!");
			}
			
		} else if (commandLabel.equalsIgnoreCase("startbroadcast")) {
			if(running == 1) {
				Player player = (Player) sender;
				player.sendMessage("They are still running!");
				
			} else {
				tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
					public void run() {
						try {
							broadcastMessage("plugins/Broadcast/messages.txt");
						} catch (IOException e) {
							
						}
					}
						
				}, 0, interval * 20);
				Player player = (Player) sender;
				player.sendMessage("Started broadcasts.");
			}
		}
		return false;
	}
}
