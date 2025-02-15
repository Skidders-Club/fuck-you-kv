package keystrokesmod.client.clickgui.kv;

import java.util.ArrayList;
import java.util.List;

import keystrokesmod.client.clickgui.kv.components.KvModuleSection;
import keystrokesmod.client.utils.RenderUtils;
import keystrokesmod.client.utils.Utils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class KvCompactGui extends GuiScreen {

	private KvSection currentSection;
	private List<KvSection> sections;

    public static int containerX, containerY, containerWidth, containerHeight, horizontalBoarderY;


    public KvCompactGui() {
		sections = new ArrayList<KvSection>();
		sections.add(currentSection = new KvModuleSection());
		sections.add(new KvSection("seggs"));
		sections.add(new KvSection("terminal"));
    }

	@Override
	public void initGui() {
        containerWidth = (int) (this.width/1.5);
        containerHeight = (int) (this.height/1.5);
        containerX = (this.width/2) - (containerWidth/2);
        containerY = (this.height/2) - (containerHeight/2);
        horizontalBoarderY = containerY + (containerHeight/6);
		KvModuleSection.initGui(containerX, containerY, containerWidth, containerHeight);
		currentSection.refresh();
		int xOffSet = 0;
		for (KvSection section : sections) {
			section.setSectionCoords(containerX + (containerWidth/3) + xOffSet, containerY + ((containerHeight/6/2 ) - (section.getHeight()/2)));
			xOffSet += section.getWidth() + 5;
		}
    }

    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //drawing container
		RenderUtils.drawBorderedRoundedRect(
                containerX,
                containerY,
                containerX + containerWidth,
                containerY + containerHeight,
                7,
                3,
				Utils.Client.rainbowDraw(1, 0), 0x80000000);

		//drawing horizontal boarder
        Gui.drawRect(
                containerX,
                horizontalBoarderY,
                containerX + containerWidth,
                horizontalBoarderY + 1,
                Utils.Client.rainbowDraw(1, 0));

		for (KvSection section : sections)
			section.drawSection(mouseX, mouseY, partialTicks);

		currentSection.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (KvSection section : sections) {
			if (section.mouseClicked(mouseX, mouseY, mouseButton));
			return;
		}
	}

	public KvSection getCurrentSection() {
		return currentSection;
	}

	public void setCurrentSection(KvSection section) {
		currentSection = section;
    }
}
