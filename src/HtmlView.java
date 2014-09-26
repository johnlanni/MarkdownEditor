import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.petebevin.markdown.MarkdownProcessor;


public class HtmlView implements Observer{
	private final JScrollPane jspane = new JScrollPane();
	private final JEditorPane jepane = new JEditorPane("text/html","");
	
	public HtmlView(){
		jepane.setEditable(false);
		jspane.getViewport().add(jepane, null);		
	}
	public void update(final Observable o,  final Object data) {
		if (o instanceof MarkdownInput) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String text = (String)data;
					MarkdownProcessor processor = new MarkdownProcessor();
					jepane.setText(processor.markdown(text).replaceAll("src=\"", "src=\"file:"));		        		
				}
			});
		}
	}
	public JScrollPane getpane() {
		return jspane;
	}
	

}
