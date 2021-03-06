package org.esn.mobilit.utils.parser;

import com.bumptech.glide.Glide;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.models.RSS.RSS;
import org.esn.mobilit.models.RSS.RSSItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.esn.mobilit.utils.Reversed.reversed;

public class RSSFeedParser implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<RSSItem> itemlist;

	public RSSFeedParser() {
		itemlist = new ArrayList<>();
	}

    public RSSFeedParser(List<RSSItem> itemlist){
        this.itemlist = itemlist;
    }

	public void addItem(RSSItem rssItem) {
		itemlist.add(0, rssItem);
	}

	public RSSItem getItem(int location) {
		return itemlist.get(location);
	}

    public List<RSSItem> getList(){
		return itemlist;
	}

	public int getItemCount() {
		return getList().size();
	}

	public RSSItem getRSSItemFromTitle(String title) {
		title = title.replaceAll("\\s+","");
		for(RSSItem item : this.getList()) {
			String itemTitle = item.getTitle().replaceAll("\\s+","");
			if (itemTitle.equalsIgnoreCase(title)) {
				return item;
			}
		}
		return null;
	}

	/**
	 *
	 * @param link Link of the node
	 * @return int position of the item in the list, -1 if not present
	 */
	public int getIndex(List<RSSItem> list, String link) {
		int i = 0;
		for(RSSItem item : list){
			if (item.getLink().equalsIgnoreCase(link)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	public RSSFeedParser updateItems(List<RSSItem> serverItems) {
		if (serverItems.size() == 0) {
			return this;
		}

		for (RSSItem serverItem : serverItems) {
			moveImage(serverItem);
		}

		this.itemlist = serverItems;

		return this;
	}

	public void moveImage(RSSItem rssItem){
		DOMParser.moveImage(rssItem);

		Glide.with(MobilITApplication.getContext())
				.load(rssItem.getImage())
				.preload(150, 250);
	}
}
