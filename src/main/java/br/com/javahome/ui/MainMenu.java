package br.com.javahome.ui;

import br.com.javahome.ApplicationContext;
import br.com.javahome.di.Qualifier;
import br.com.javahome.enums.ViewType;
import br.com.javahome.event.EventBus;
import br.com.javahome.event.EventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static br.com.javahome.ui.RoomUI.DEFAULT;
import static java.awt.Color.BLACK;

public class MainMenu extends JPanel {

    private static final int MENU_HEIGHT = 30;
    private RoomUI selectedRoomUI;
    private final EventBus eventBus;
    private final DateTimeFormatter dateTimeFormatter;

    public MainMenu(@Qualifier("leftPanel") final JPanel leftPanel, @Qualifier("rightPanel") final JPanel rightPanel,
                    final EventBus eventBus, final DateTimeFormatter dateTimeFormatter) {
        this.eventBus = eventBus;
        this.dateTimeFormatter = dateTimeFormatter;
        initLayout();

        JButton previewBtn = initPreviewBtn(leftPanel, rightPanel);
        JLabel dateTime = new JLabel();
        dateTime.setForeground(DEFAULT);
        add(dateTime, BorderLayout.EAST);

        startClock(dateTime);
        subscriberEvents(eventBus, previewBtn);
    }

    private void initLayout() {
        setPreferredSize(new Dimension(0, MENU_HEIGHT));
        setBorder(BorderFactory.createEmptyBorder(0,7,7,7));
        setBackground(BLACK);
        setLayout(new BorderLayout());
    }

    private JButton initPreviewBtn(JPanel leftPanel, JPanel rightPanel) {
        JButton previewBtn = createPreviewBtn();
        previewBtn.addMouseListener(createHoverListener());
        previewBtn.addActionListener(createPreviewAction(leftPanel, rightPanel));
        previewBtn.setVisible(false);

        add(previewBtn, BorderLayout.WEST);
        return previewBtn;
    }

    private void startClock(final JLabel dateTime) {
        new Timer(1000, e -> dateTime.setText(dateTimeFormatter.format(LocalDateTime.now()))).start();
    }

    private void subscriberEvents(EventBus eventBus, JButton previewBtn) {
        eventBus.subscribe(EventType.ROOM_SELECTED, room -> {
            SwingUtilities.invokeLater(() -> {
                selectedRoomUI = (RoomUI) room;
                previewBtn.setVisible(room != null);
            });
        });
    }

    private JButton createPreviewBtn() {
        URL resource = getClass().getResource("/icons/Preview/left-arrow.png");
        Objects.requireNonNull(resource, "Icon /icons/Preview/left-arrow.png not found.");

        PreviewButton previewBtn = new PreviewButton();
        previewBtn.setIcon(new ImageIcon(resource));
        previewBtn.setText(ApplicationContext.getProperty("main.menu.preview-btn"));
        previewBtn.setHorizontalAlignment(SwingConstants.LEFT);
        previewBtn.setPreferredSize(new Dimension(65, MENU_HEIGHT - 8));
        return previewBtn;
    }

    private MouseAdapter createHoverListener() {
        return new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                PreviewButton btn = (PreviewButton) e.getComponent();
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btn.setAlpha(0.70f);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                PreviewButton btn = (PreviewButton) e.getComponent();
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                btn.setAlpha(1f);
            }
        };
    }

    private ActionListener createPreviewAction(final JPanel leftPanel, final JPanel rightPanel) {
        return e -> {
            ViewType preview = ApplicationContext.currentViewType.preview();

            if (preview == null) return ;

            CardLayout layout = (CardLayout) rightPanel.getLayout();
            layout.show(rightPanel, preview.name());
            ApplicationContext.currentViewType = preview;
            if(preview == ViewType.HOME_DETAIL){
                if(selectedRoomUI != null) {
                    selectedRoomUI.disable();
                }
                ApplicationContext.roomTypeCurrent = null;
                CardLayout rightSideLayout = (CardLayout) leftPanel.getLayout();
                rightSideLayout.show(leftPanel, ViewType.HOME.name());
                eventBus.publish(EventType.ROOM_SELECTED, () -> null);
            }
        };
    }
}
