package catan.common.launcher;

import catan.client.ClientImpl;
import catan.common.CoreWindow;
import catan.server.ServerImpl;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Greg on 8/10/2014.
 * Launch window to select further actions. Replaces the command-line interface.
 */
public class GraphicalLauncher extends CoreWindow {

    private final Logger logger = Logger.getLogger(getClass().getName());

    public GraphicalLauncher(StartupOptions options) {
        //TODO: Clean up the graphical launcher to include features from the command line.
        super("Settlers of Catan - Launcher", new Dimension(300, 500), false);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        setLayout(null);

        int j = 0;
        JButton[] buttons = new JButton[ActionButton.values().length];
        for (ActionButton ab : ActionButton.values()) {
            JButton button = new JButton(ab.label);
            button.setBounds(ab.getBounds());
            button.setHorizontalAlignment(SwingConstants.CENTER);
            contentPane.add(button);
            buttons[j] = button;
            j++;
        }

        buttons[0].addActionListener(e -> {
            try {
                startClient();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error starting client", ex);
            }
        });
        buttons[1].addActionListener(e -> {
            try {
                startServer();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error while starting server", ex);
            }
        });
        display();
        setVisible(true);
    }


    private void startClient() {
        new ClientImpl();
    }

    private void startServer() {
        new ServerImpl(25000);
    }

    @Override
    protected void onClose() {
        dispose();
    }

    @Override
    protected void onResize(Dimension size) { }

    public String toString() {
        return "GraphicalLauncher";
    }


    private enum ActionButton {

        Client("Start Client", 80, 350, 120, 24),
        Server("Start Server", 220, 350, 120, 24);

        private final String label;
        private final Point position;
        private final Dimension size;

        ActionButton(String label, int x, int y, int w, int h) {
            this.label = label;
            position = new Point(x - (w / 2), y - (h / 2));
            size = new Dimension(w, h);
        }

        private Rectangle getBounds() {
            return new Rectangle(position, size);
        }
    }

}
