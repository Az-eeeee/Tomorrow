package tomorrow.tomo.commands.commands;

import tomorrow.tomo.commands.Command;

public class gc extends Command {
    public gc() {
        super("gc", new String[]{}, "", "Just free memory");
    }

    @Override
    public String execute(String[] var1) {
        System.gc();
        return null;
    }
}
