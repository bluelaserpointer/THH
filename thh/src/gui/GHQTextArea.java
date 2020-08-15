package gui;

import javax.swing.JTextArea;

import core.GHQ;

public class GHQTextArea extends GUIParts {
	private final JTextArea jta = new JTextArea();
	{
		jta.setOpaque(false);
		jta.setLineWrap(true);
		if(GHQ.initialFont != null)
			jta.setFont(GHQ.initialFont.deriveFont(40F));
	}
	@Override
	public void paint() {
		super.paint();
		final int TX = point().intX(), TY = point().intY();
		GHQ.getG2D().translate(TX, TY);
		jta.paint(GHQ.getG2D());
		GHQ.getG2D().translate(-TX, -TY);
	}
	@Override
	public GHQTextArea setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		jta.setBounds(x, y, w, h);
		return this;
	}
	//information
	public JTextArea textArea() {
		return jta;
	}
}
