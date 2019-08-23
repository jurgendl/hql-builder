package org.tools.hqlbuilder.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.slf4j.LoggerFactory;

/**
 * @author Jurgen
 */
public abstract class Action extends AbstractAction implements PropertyChangeListener {
    private static final long serialVersionUID = 7743926766652503512L;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Action.class);

    private transient Preferences persister;

    private Class<?> type;

    private Object value;

    private String description;

    private boolean warnRestart = false;

    private final String id;

    public Action(String id, boolean enabled, String name, Icon icon, String shortDescription, String longDescription, Boolean selected,
            Character mnemonic, String accelerator) {
        this(id, enabled, name, icon, shortDescription, longDescription, selected, mnemonic, accelerator, false, null, null, null);
    }

    public Action(String id, boolean enabled, String name, Icon icon, String shortDescription, String longDescription, Boolean selected,
            Character mnemonic, String accelerator, String parentId) {
        this(id, enabled, name, icon, shortDescription, longDescription, selected, mnemonic, accelerator, true, parentId, Boolean.class, null);
    }

    public <T> Action(String id, boolean enabled, String name, Icon icon, String shortDescription, String longDescription, Boolean selected,
            Character mnemonic, String accelerator, String parentId, Class<T> type, T defaultValue) {
        this(id, enabled, name, icon, shortDescription, longDescription, selected, mnemonic, accelerator, true, parentId, type, defaultValue);
    }

    public <T> Action(String id, boolean enabled, String name, Icon icon, String shortDescription, String longDescription, Boolean selected,
            Character mnemonic, String accelerator, boolean persist, String parentId, Class<T> type, T defaultValue) {
        logger.debug("{} {}", id, value);
        this.id = id;
        if (persist && parentId != null) {
            if (id == null || "null".equals(id)) {
                throw new NullPointerException();
            }
            logger.debug("persistent: {}", id);
            this.persister = Preferences.userRoot().node(parentId);
            if (Boolean.class.equals(type)) {
                this.type = Boolean.class;
                this.value = selected = load(selected);
            } else {
                this.type = type;
                this.value = load(defaultValue);
            }
        }
        super.putValue(SELECTED_KEY, selected);

        if (!Color.class.equals(type)) {
            setIcon(icon);
        }

        setName(name);
        setEnabled(enabled);
        description = shortDescription;
        setDescriptions(value);
        setMnemonic(mnemonic);
        setAccelerator(accelerator);
        addPropertyChangeListener(this);
    }

    @SuppressWarnings("unchecked")
    protected <T> T load(T defaultValue) {
        if (persister == null) {
            return defaultValue;
        }
        T actualValue;
        if (Color.class.equals(type)) {
            int i = persister.getInt(id, Integer.MIN_VALUE);
            actualValue = i == Integer.MIN_VALUE ? null : (T) new Color(i);
            if (actualValue != null) {
                setColorIcon(new Color(i));
            }
        } else if (Font.class.equals(type)) {
            Font defaultFont = (Font) defaultValue;
            String family = persister.get(id + ".name", defaultFont.getFamily());
            int size = persister.getInt(id + ".size", defaultFont.getSize());
            int style = persister.getInt(id + ".style", defaultFont.getStyle());
            Font font = new Font(family, style, size);
            actualValue = (T) font;
        } else if (Boolean.class.equals(type)) {
            actualValue = (T) (Boolean) persister.getBoolean(id, Boolean.class.cast(defaultValue));
        } else if (String.class.equals(type)) {
            actualValue = (T) persister.get(id, String.class.cast(defaultValue));
        } else if (Double.class.equals(type)) {
            actualValue = (T) (Double) persister.getDouble(id, Double.class.cast(defaultValue));
        } else if (Float.class.equals(type)) {
            actualValue = (T) (Float) persister.getFloat(id, Float.class.cast(defaultValue));
        } else if (Integer.class.equals(type)) {
            actualValue = (T) (Integer) persister.getInt(id, Integer.class.cast(defaultValue));
        } else if (Long.class.equals(type)) {
            actualValue = (T) (Long) persister.getLong(id, Long.class.cast(defaultValue));
        } else if (byte[].class.equals(type)) {
            actualValue = (T) persister.getByteArray(id, byte[].class.cast(defaultValue));
        } else {
            throw new IllegalArgumentException(String.valueOf(type));
        }
        logger.debug("persistent: {}: load={}", id, actualValue);
        return actualValue;
    }

    protected void setColorIcon(Color c) {
        BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(Color.black);
        g2.fillRect(0, 0, 16, 16);
        g2.setColor(c);
        g2.fillRect(1, 1, 14, 14);
        setIcon(new ImageIcon(bi));
    }

    protected void save(Object newValue) {
        this.value = newValue;
        if (persister == null) {
            return;
        }
        logger.debug("persistent: {}: save={}", id, newValue);
        if (Color.class.equals(type)) {
            setColorIcon((Color) newValue);
            persister.putInt(id, newValue == null ? Integer.MIN_VALUE : Color.class.cast(newValue).getRGB());
        } else if (Font.class.equals(type)) {
            Font font = (Font) newValue;
            persister.put(id + ".name", font.getFamily());
            persister.putInt(id + ".size", font.getSize());
            persister.putInt(id + ".style", font.getStyle());
        } else if (Boolean.class.equals(type)) {
            persister.putBoolean(id, Boolean.class.cast(newValue));
        } else if (String.class.equals(type)) {
            persister.put(id, String.class.cast(newValue));
        } else if (Double.class.equals(type)) {
            persister.putDouble(id, Double.class.cast(newValue));
        } else if (Float.class.equals(type)) {
            persister.putFloat(id, Float.class.cast(newValue));
        } else if (Integer.class.equals(type)) {
            persister.putInt(id, Integer.class.cast(newValue));
        } else if (Long.class.equals(type)) {
            persister.putLong(id, Long.class.cast(newValue));
        } else if (byte[].class.equals(type)) {
            persister.putByteArray(id, byte[].class.cast(newValue));
        } else {
            throw new IllegalArgumentException(String.valueOf(type));
        }

        if (warnRestart) {
            JOptionPane.showMessageDialog(null, HqlResourceBundle.getMessage("change visible after restart"), "", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setSelected(Boolean selected) {
        super.putValue(SELECTED_KEY, selected);
        save(selected);
    }

    public void setValue(Object value) {
        save(value);
        setDescriptions(value);
    }

    protected void setDescriptions(Object value) {
        if (type == null || persister == null) {
            setShortDescription(description);
            setLongDescription(description);
            return;
        }
        if (Color.class.equals(type) || Boolean.class.equals(type)) {
            setShortDescription(description);
            setLongDescription(description);
            return;
        }
        if (value == null) {
            setShortDescription(description + ": -");
            setLongDescription(description + ": -");
            return;
        }
        if (Font.class.equals(type)) {
            Font f = (Font) value;
            value = f.getFontName() + " " + f.getSize();
        }
        setShortDescription(description + ": " + value);
        setLongDescription(description + ": " + value);
    }

    public Boolean isSelected() {
        return (Boolean) super.getValue(SELECTED_KEY);
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    public String getName() {
        return (String) super.getValue(javax.swing.Action.NAME);
    }

    public void setName(String name) {
        update(name, getMnemonic());
    }

    public String getShortDescription() {
        return (String) super.getValue(javax.swing.Action.SHORT_DESCRIPTION);
    }

    public void setShortDescription(String shortDescription) {
        super.putValue(SHORT_DESCRIPTION, shortDescription);
    }

    public String getLongDescription() {
        return (String) super.getValue(javax.swing.Action.LONG_DESCRIPTION);
    }

    public void setLongDescription(String longDescription) {
        super.putValue(LONG_DESCRIPTION, longDescription);
    }

    public Icon getIcon() {
        return (Icon) super.getValue(SMALL_ICON);
    }

    public void setIcon(Icon icon) {
        super.putValue(SMALL_ICON, icon);
    }

    public Character getMnemonic() {
        Integer c = (Integer) super.getValue(MNEMONIC_KEY);
        if (c == null) {
            return null;
        }
        int cc = c;
        return (char) cc;
    }

    public void setMnemonic(Character mnemonic) {
        update(getName(), mnemonic);
    }

    public KeyStroke getAccelerator() {
        return (KeyStroke) super.getValue(ACCELERATOR_KEY);
    }

    public void setAccelerator(KeyStroke keyStroke) {
        super.putValue(ACCELERATOR_KEY, keyStroke);
    }

    public void setAccelerator(String accelerator) {
        if (accelerator == null) {
            setAccelerator((KeyStroke) null);
        } else {
            setAccelerator(KeyStroke.getKeyStroke(accelerator));
        }
    }

    protected void update(String name, Character mnemonic) {
        int index = -1;
        Integer mnemo = mnemonic == null ? null : (int) mnemonic;

        if (name != null && mnemonic != null) {
            index = name.toLowerCase().indexOf(Character.toLowerCase(mnemonic));

            if (index == -1) {
                name = name + " (" + mnemonic + ")";
                index = name.toLowerCase().indexOf(Character.toLowerCase(mnemonic));
            }
        }

        super.putValue(MNEMONIC_KEY, mnemo);
        super.putValue(NAME, name);
        super.putValue(ACTION_COMMAND_KEY, name);
        super.putValue(DISPLAYED_MNEMONIC_INDEX_KEY, index);
    }

    /**
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    final public void propertyChange(PropertyChangeEvent evt) {
        if (persister != null && SELECTED_KEY.equals(evt.getPropertyName())) {
            save(evt.getNewValue());
        }
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object key : getKeys()) {
            sb.append(key).append("=").append(getValue((String) key)).append(";");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public String getId() {
        return this.id;
    }

    public boolean isWarnRestart() {
        return this.warnRestart;
    }

    public void setWarnRestart(boolean warnRestart) {
        this.warnRestart = warnRestart;
    }
}
