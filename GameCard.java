import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ============================================================
 *             GAMESMANIA • VINTAGE ARCADE CARD
 * ============================================================
 *
 * Rich, aesthetic, vintage 1980s Arcade Cabinet Game Card:
 *  - 60 FPS interpolated elevation & floating physics
 *  - Authentic Marquee Chaser Bulbs (7 animated sequencing lights on hover)
 *  - Vintage Arcade Cabinet chassis with aged brass rivets & beveled frame
 *  - Curved CRT Monitor screen with horizontal phosphor scanlines
 *  - Vintage Marquee headers: "★ 1980s COIN-OP ★" & "● INSERT COIN ●"
 *  - Retro Arcade Dipswitch Difficulty Stamp ([ LEVEL: HARD ★★★ ])
 *  - High-Score Credit indicator & Star Rating (4.9 ★)
 *  - 3D Convex Arcade "PRESS START ▶" Push-Button with hover glow
 *  - Diagonal golden shimmer sweep & sound feedback on click
 * ============================================================
 */
public class GameCard extends JPanel {

    private final String gameName;
    private final String description;
    private final String icon;
    private final String difficulty;
    private final String category;
    private final Color accentColor;
    private final boolean isFeatured;
    private final String rankBadge;
    private final String ratingText;
    private final String vintageCabinetTag;
    private final String xpReward;

    // Animation states
    private boolean hovered = false;
    private float hoverProgress = 0f;
    private float shimmerProgress = 0f;
    private float glowAngle = 0f;
    private float pulsePhase = 0f;
    private Timer animationTimer;
    private Runnable clickAction;

    public GameCard(
            String gameName,
            String description,
            String icon,
            String difficulty,
            Color accentColor) {
        this(gameName, description, icon, difficulty, accentColor, false, "");
    }

    public GameCard(
            String gameName,
            String description,
            String icon,
            String difficulty,
            Color accentColor,
            boolean isFeatured,
            String rankBadge) {

        this.gameName = gameName;
        this.description = description;
        this.icon = icon;
        this.difficulty = (difficulty != null && !difficulty.trim().isEmpty()) ? difficulty : "Medium";
        this.accentColor = (accentColor != null) ? accentColor : new Color(245, 158, 11);
        this.category = detectCategory(gameName);
        this.isFeatured = isFeatured;
        this.rankBadge = rankBadge;
        this.ratingText = getEstimatedRating(gameName);
        this.vintageCabinetTag = detectCabinetTag(gameName, isFeatured);
        this.xpReward = detectXpReward(difficulty);

        setOpaque(false);
        if (isFeatured) {
            setPreferredSize(new Dimension(292, 226));
        } else {
            setPreferredSize(new Dimension(272, 216));
        }
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setupMouseEvents();
        setupAnimation();
    }

    private String detectCategory(String name) {
        if (name == null) return "ARCADE";
        String n = name.toLowerCase();
        if (n.contains("puzzle") || n.contains("2048") || n.contains("sudoku") || n.contains("guess") || n.contains("color")) {
            return "PUZZLE";
        } else if (n.contains("spin") || n.contains("coin") || n.contains("roulette")) {
            return "CASINO";
        } else if (n.contains("pong") || n.contains("snake") || n.contains("tic")) {
            return "RETRO";
        } else {
            return "ACTION";
        }
    }

    private String detectCabinetTag(String name, boolean featured) {
        if (featured) return "1984 COIN-OP";
        String n = (name != null) ? name.toLowerCase() : "";
        if (n.contains("snake") || n.contains("pong") || n.contains("asteroid")) return "VINTAGE '82";
        if (n.contains("dino") || n.contains("archer") || n.contains("racing")) return "DELUXE CAB";
        return "ARCADE CAB";
    }

    private String detectXpReward(String diff) {
        if ("Hard".equalsIgnoreCase(diff)) return "+350 XP";
        if ("Medium".equalsIgnoreCase(diff)) return "+250 XP";
        return "+150 XP";
    }

    private String getEstimatedRating(String name) {
        if (name == null) return "4.8 ★";
        String n = name.toLowerCase();
        if (n.contains("2048")) return "4.9 ★";
        if (n.contains("asteroid")) return "4.9 ★";
        if (n.contains("snake")) return "4.8 ★";
        if (n.contains("dino")) return "4.8 ★";
        if (n.contains("archer")) return "4.7 ★";
        if (n.contains("sudoku")) return "4.8 ★";
        if (n.contains("pong")) return "4.7 ★";
        return "4.8 ★";
    }

    public String getGameName() {
        return gameName;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setClickAction(Runnable action) {
        this.clickAction = action;
    }

    private void setupMouseEvents() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                shimmerProgress = 0f;
                startAnimation();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                startAnimation();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                SettingFrame.playTestSound();
                if (clickAction != null) {
                    clickAction.run();
                }
            }
        });
    }

    private void setupAnimation() {
        animationTimer = new Timer(16, e -> {
            if (hovered) {
                hoverProgress = Math.min(1f, hoverProgress + 0.12f);
                shimmerProgress = Math.min(1.5f, shimmerProgress + 0.045f);
                glowAngle = (glowAngle + 0.07f) % ((float) Math.PI * 2f);
                pulsePhase = (pulsePhase + 0.14f) % ((float) Math.PI * 2f);
            } else {
                hoverProgress = Math.max(0f, hoverProgress - 0.12f);
                shimmerProgress = Math.max(0f, shimmerProgress - 0.08f);
            }

            repaint();

            if (!hovered && hoverProgress <= 0f && shimmerProgress <= 0f) {
                animationTimer.stop();
            }
        });
    }

    private void startAnimation() {
        if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

        int width = getWidth();
        int height = getHeight();

        int lift = (int) (hoverProgress * 8);
        int cardX = 4;
        int cardY = 4 - lift;
        int cardWidth = width - 8;
        int cardHeight = height - 8;

        // 1. SOFT MULTI-LAYER VINTAGE SHADOW
        int shadowAlpha = theme.isDark ? (32 + (int) (hoverProgress * 55)) : (20 + (int) (hoverProgress * 42));
        g2.setColor(new Color(0, 0, 0, shadowAlpha));
        g2.fillRoundRect(cardX + 2, cardY + 7 + lift, cardWidth - 4, cardHeight - 2, 22, 22);

        if (hoverProgress > 0) {
            // Warm amber neon ambient bloom around vintage cabinet
            int glowAlpha = theme.isDark ? (int) (hoverProgress * 55) : (int) (hoverProgress * 35);
            g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), glowAlpha));
            g2.fillRoundRect(cardX - 2, cardY - 2, cardWidth + 4, cardHeight + 4, 24, 24);
        }

        // 2. VINTAGE CABINET CHASSIS BACKGROUND
        // Warm woodgrain / aged bakelite depth gradient
        Color topCardColor = theme.cardBg;
        Color bottomCardColor = theme.isDark
                ? new Color(
                        Math.max(0, theme.cardBg.getRed() - 8),
                        Math.max(0, theme.cardBg.getGreen() - 8),
                        Math.max(0, theme.cardBg.getBlue() - 8))
                : new Color(
                        Math.max(0, theme.cardBg.getRed() - 4),
                        Math.max(0, theme.cardBg.getGreen() - 4),
                        Math.max(0, theme.cardBg.getBlue() - 4));

        GradientPaint chassisGrad = new GradientPaint(cardX, cardY, topCardColor, cardX, cardY + cardHeight, bottomCardColor);
        g2.setPaint(chassisGrad);
        g2.fillRoundRect(cardX, cardY, cardWidth, cardHeight, 18, 18);

        // 3. DIAGONAL GOLDEN SHIMMER SWEEP ON HOVER
        if (hoverProgress > 0 && shimmerProgress > 0) {
            Shape oldClip = g2.getClip();
            g2.clip(new java.awt.geom.RoundRectangle2D.Float(cardX, cardY, cardWidth, cardHeight, 18, 18));

            int shimmerX = (int) (cardX - cardWidth + (shimmerProgress * (cardWidth * 2)));
            GradientPaint shimmerPaint = new GradientPaint(
                    shimmerX, cardY, new Color(255, 255, 255, 0),
                    shimmerX + 50, cardY, new Color(254, 240, 138, theme.isDark ? 55 : 85), // Warm gold highlight
                    true
            );
            g2.setPaint(shimmerPaint);
            g2.fillRect(cardX, cardY, cardWidth, cardHeight);
            g2.setClip(oldClip);
        }

        // 4. VINTAGE CABINET BEVEL & AGED BRASS BORDER
        if (hoverProgress > 0) {
            // Glowing neon marquee rim
            Color borderC1 = new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), (int) (hoverProgress * 235));
            Color borderC2 = new Color(245, 158, 11, (int) (hoverProgress * 180));
            GradientPaint gpBorder = new GradientPaint(cardX, cardY, borderC1, cardX + cardWidth, cardY + cardHeight, borderC2);
            g2.setPaint(gpBorder);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawRoundRect(cardX, cardY, cardWidth - 1, cardHeight - 1, 18, 18);
        } else {
            // Elegant antique brass chassis border
            g2.setColor(theme.cardBorder);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(cardX, cardY, cardWidth - 1, cardHeight - 1, 18, 18);

            // Subtle inner inset bevel line for vintage cabinet depth
            g2.setColor(new Color(255, 255, 255, theme.isDark ? 12 : 25));
            g2.drawRoundRect(cardX + 2, cardY + 2, cardWidth - 5, cardHeight - 5, 16, 16);
        }

        // 5. FOUR VINTAGE BRASS CORNER RIVETS / STUDS
        drawBrassRivet(g2, cardX + 8, cardY + 8, theme);
        drawBrassRivet(g2, cardX + cardWidth - 14, cardY + 8, theme);
        drawBrassRivet(g2, cardX + 8, cardY + cardHeight - 14, theme);
        drawBrassRivet(g2, cardX + cardWidth - 14, cardY + cardHeight - 14, theme);

        // 6. ANIMATED RETRO MARQUEE CHASER BULBS (7 lights along top center)
        int bulbCount = 7;
        int activeBulb = (int) (pulsePhase * 3.5) % bulbCount;
        int startBulbX = cardX + (cardWidth - (bulbCount * 12)) / 2;
        int bulbY = cardY + 6;

        for (int b = 0; b < bulbCount; b++) {
            int bx = startBulbX + (b * 12);
            boolean isLit = (hoverProgress > 0) && (b == activeBulb || b == (activeBulb + 1) % bulbCount);

            if (isLit) {
                // Glowing golden amber marquee bulb
                g2.setColor(new Color(254, 240, 138, 220));
                g2.fillOval(bx - 1, bulbY - 1, 6, 6);
                g2.setColor(new Color(245, 158, 11));
                g2.fillOval(bx, bulbY, 4, 4);
            } else {
                // Soft filament brass bulb
                g2.setColor(new Color(180, 83, 9, theme.isDark ? 110 : 80));
                g2.fillOval(bx, bulbY, 4, 4);
            }
        }

        // 7. CURVED CRT MONITOR SCREEN WITH PHOSPHOR SCANLINES
        int iconSize = isFeatured ? 48 : 44;
        int iconX = cardX + 16;
        int iconY = cardY + 20;

        // Dark CRT Bezel Background
        Color crtBg = theme.isDark ? new Color(15, 18, 24) : new Color(28, 25, 23);
        g2.setColor(crtBg);
        g2.fillRoundRect(iconX, iconY, iconSize, iconSize, 12, 12);

        // Phosphor scanlines texture (alternating 1px horizontal raster lines)
        Shape oldClipCRT = g2.getClip();
        g2.clip(new java.awt.geom.RoundRectangle2D.Float(iconX, iconY, iconSize, iconSize, 12, 12));
        g2.setColor(new Color(0, 0, 0, 65));
        for (int sy = iconY; sy < iconY + iconSize; sy += 3) {
            g2.drawLine(iconX, sy, iconX + iconSize, sy);
        }

        // Curved CRT Glass Reflection Arc (Top-Left)
        g2.setColor(new Color(255, 255, 255, hoverProgress > 0 ? 55 : 30));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawArc(iconX + 3, iconY + 3, iconSize - 6, iconSize - 6, 95, 80);
        g2.setClip(oldClipCRT);

        // CRT Bezel Rim
        Color crtBorder = hoverProgress > 0 ? accentColor : new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 90);
        g2.setColor(crtBorder);
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawRoundRect(iconX, iconY, iconSize - 1, iconSize - 1, 12, 12);

        // Phosphor Halo & Emoji Icon
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, isFeatured ? 26 : 23));
        FontMetrics fmEmoji = g2.getFontMetrics();
        int ex = iconX + (iconSize - fmEmoji.stringWidth(icon)) / 2;
        int ey = iconY + (iconSize + fmEmoji.getAscent() - fmEmoji.getDescent()) / 2;
        g2.drawString(icon, ex, ey);

        // 8. VINTAGE MARQUEE RIBBONS & CABINET BADGES
        int badgeH = 22;
        int rightMargin = cardX + cardWidth - 14;

        if (isFeatured && rankBadge != null && !rankBadge.isEmpty()) {
            // Golden Retro Trophy Ribbon (#1 TOP)
            int rankW = 76;
            int rankX = rightMargin - rankW;
            g2.setColor(new Color(245, 158, 11, 45));
            g2.fillRoundRect(rankX, iconY, rankW, badgeH, 8, 8);
            g2.setColor(new Color(245, 158, 11));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(rankX, iconY, rankW - 1, badgeH - 1, 8, 8);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            FontMetrics fmRank = g2.getFontMetrics();
            g2.drawString("★ " + rankBadge, rankX + (rankW - fmRank.stringWidth("★ " + rankBadge)) / 2, iconY + 15);
        } else {
            // Vintage Category Ribbon
            int catW = 78;
            int catX = rightMargin - catW;
            g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), theme.isDark ? 40 : 25));
            g2.fillRoundRect(catX, iconY, catW, badgeH, 8, 8);
            g2.setColor(accentColor);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(catX, iconY, catW - 1, badgeH - 1, 8, 8);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            FontMetrics fmTag = g2.getFontMetrics();
            g2.drawString("🕹 " + category, catX + (catW - fmTag.stringWidth("🕹 " + category)) / 2, iconY + 15);
        }

        // Secondary Vintage Cabinet Tag (e.g. "1984 COIN-OP" or "VINTAGE '82")
        int cabW = 74;
        int cabX = rightMargin - cabW - (isFeatured ? 82 : 84);
        g2.setColor(new Color(180, 83, 9, theme.isDark ? 35 : 20));
        g2.fillRoundRect(cabX, iconY, cabW, badgeH, 8, 8);
        g2.setColor(new Color(217, 119, 6));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
        FontMetrics fmCab = g2.getFontMetrics();
        g2.drawString(vintageCabinetTag, cabX + (cabW - fmCab.stringWidth(vintageCabinetTag)) / 2, iconY + 15);

        // 9. GAME TITLE & VINTAGE GOLD STAR RATING
        g2.setColor(theme.textPrimary);
        g2.setFont(new Font("Segoe UI", Font.BOLD, isFeatured ? 17 : 16));
        g2.drawString(gameName, cardX + 16, cardY + 88);

        // Star Rating
        g2.setColor(new Color(245, 158, 11));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        FontMetrics fmRat = g2.getFontMetrics();
        g2.drawString(ratingText, cardX + cardWidth - fmRat.stringWidth(ratingText) - 16, cardY + 88);

        // 10. DESCRIPTION (Cleanly truncated)
        g2.setColor(theme.textMuted);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        String desc = description;
        FontMetrics fmDesc = g2.getFontMetrics();
        int maxDescW = cardWidth - 36;
        if (fmDesc.stringWidth(desc) > maxDescW) {
            while (fmDesc.stringWidth(desc + "...") > maxDescW && desc.length() > 5) {
                desc = desc.substring(0, desc.length() - 1);
            }
            desc += "...";
        }
        g2.drawString(desc, cardX + 16, cardY + 107);

        // 11. VINTAGE ARCADE DIPSWITCH / DIFFICULTY STAMP
        int diffY = cardY + 118;
        Color diffColor = "Hard".equalsIgnoreCase(difficulty) ? new Color(239, 68, 68)
                : ("Easy".equalsIgnoreCase(difficulty) ? new Color(16, 185, 129) : new Color(245, 158, 11));

        // Vintage Dipswitch Stamp: [ LEVEL: HARD ★★★ ]
        String stars = "Hard".equalsIgnoreCase(difficulty) ? "★★★" : ("Medium".equalsIgnoreCase(difficulty) ? "★★☆" : "★☆☆");
        String stampText = "DIPSWITCH: " + difficulty.toUpperCase() + " " + stars;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
        FontMetrics fmStamp = g2.getFontMetrics();
        int stampW = fmStamp.stringWidth(stampText) + 14;
        int stampH = 18;

        g2.setColor(new Color(diffColor.getRed(), diffColor.getGreen(), diffColor.getBlue(), theme.isDark ? 30 : 20));
        g2.fillRoundRect(cardX + 16, diffY, stampW, stampH, 6, 6);
        g2.setColor(diffColor);
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawRoundRect(cardX + 16, diffY, stampW - 1, stampH - 1, 6, 6);
        g2.drawString(stampText, cardX + 23, diffY + 12);

        // Coin-Op Credit Pill: "1 COIN = 1 PLAY" or XP
        String coinPillText = "🪙 1 COIN = PLAY";
        int coinW = 95;
        int coinX = cardX + cardWidth - coinW - 16;
        g2.setColor(new Color(245, 158, 11, theme.isDark ? 30 : 20));
        g2.fillRoundRect(coinX, diffY, coinW, stampH, 6, 6);
        g2.setColor(new Color(217, 119, 6));
        g2.drawRoundRect(coinX, diffY, coinW - 1, stampH - 1, 6, 6);
        g2.drawString(coinPillText, coinX + 8, diffY + 12);

        // 12. 3D CONVEX ARCADE CABINET PUSH-BUTTON ("PRESS START ▶")
        int btnH = 34;
        int btnY = cardY + cardHeight - btnH - 12;
        int btnW = cardWidth - 32;
        int btnX = cardX + 16;

        // Convex 3D Button Gradient
        Color btnTop;
        Color btnBottom;
        if (hoverProgress > 0) {
            btnTop = new Color(
                    Math.min(255, accentColor.getRed() + 40),
                    Math.min(255, accentColor.getGreen() + 40),
                    Math.min(255, accentColor.getBlue() + 40)
            );
            btnBottom = accentColor;
        } else {
            btnTop = theme.isDark ? new Color(55, 45, 36) : new Color(245, 240, 230);
            btnBottom = theme.isDark ? new Color(38, 30, 24) : new Color(225, 215, 200);
        }

        GradientPaint btnGrad = new GradientPaint(btnX, btnY, btnTop, btnX, btnY + btnH, btnBottom);
        g2.setPaint(btnGrad);
        g2.fillRoundRect(btnX, btnY, btnW, btnH, 12, 12);

        // Specular top highlight line on arcade button
        g2.setColor(new Color(255, 255, 255, hoverProgress > 0 ? 80 : 45));
        g2.drawLine(btnX + 6, btnY + 2, btnX + btnW - 6, btnY + 2);

        // Button Outer Bevel Rim
        g2.setColor(hoverProgress > 0 ? accentColor : theme.cardBorder);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(btnX, btnY, btnW - 1, btnH - 1, 12, 12);

        // Button Text & Gliding Arcade Chevron
        g2.setColor(hoverProgress > 0 ? Color.WHITE : (theme.isDark ? theme.textPrimary : new Color(180, 83, 9)));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fmBtn = g2.getFontMetrics();

        int arrowGlide = hoverProgress > 0 ? (int) (Math.sin(pulsePhase) * 3) : 0;
        String btnText = hoverProgress > 0 ? "1P PLAY NOW" : "PRESS START";
        int tx = btnX + (btnW - fmBtn.stringWidth(btnText + "  ▶")) / 2;
        g2.drawString(btnText, tx, btnY + 22);

        // Glowing Arcade Arrow
        g2.drawString(" ▶", tx + fmBtn.stringWidth(btnText) + arrowGlide, btnY + 22);

        g2.dispose();
    }

    /**
     * Helper to render vintage metallic rivets/studs at card corners
     */
    private void drawBrassRivet(Graphics2D g2, int x, int y, ThemeManager.Theme theme) {
        g2.setColor(new Color(180, 83, 9, theme.isDark ? 160 : 120)); // Brass base
        g2.fillOval(x, y, 6, 6);
        g2.setColor(new Color(254, 240, 138, theme.isDark ? 220 : 180)); // Specular highlight
        g2.fillOval(x + 1, y + 1, 2, 2);
    }
}