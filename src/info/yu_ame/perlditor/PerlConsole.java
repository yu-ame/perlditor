package info.yu_ame.perlditor;

import java.io.IOException;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class PerlConsole {
	private static final String consoleName = "perlditor console";
	
	public static void println(String text){
		MessageConsole console = findConsole(consoleName);
		MessageConsoleStream stream = console.newMessageStream();
		stream.print(text);
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    private static MessageConsole findConsole(String name) {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();
        for (int i = 0; i < existing.length; i++)
            if (name.equals(existing[i].getName()))
                return (MessageConsole) existing[i];
        // no console found, so create a new one
        MessageConsole myConsole = new MessageConsole(name, null);
        conMan.addConsoles(new IConsole[] { myConsole });
        return myConsole;
    }    	
}
