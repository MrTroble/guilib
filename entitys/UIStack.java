package eu.gir.girsignals.guis.guilib.entitys;


public class UIStack extends UIComponent {

	
	public UIStack() {}
	
	@Override
	public void draw(int mouseX, int mouseY) {}

	@Override
	public void update() {
		parent.children.forEach(e -> {
			if(e.inheritWidth())
				e.setWidth(parent.getWidth());
			if(e.inheritHeight())
				e.setHeight(parent.getHeight());
		});
	}
	
}
