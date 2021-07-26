package tomorrow.tomo.commands.commands;

import tomorrow.tomo.Client;
import tomorrow.tomo.commands.Command;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.utils.cheats.player.Helper;

public class Reload extends Command {
    public Reload() {
        super("reload", new String[]{"reloadmods"}, "", "reload all modules(config may lose or crash)");
    }

    @Override
    public String execute(String[] var1) {
        ModuleManager.modules.clear();
        ModuleManager.enabledModules.clear();

        Client.instance.getModuleManager().init();
        Helper.sendMessage("Reloaded");
        return null;
    }
}
