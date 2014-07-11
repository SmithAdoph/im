package com.lejoying.wxgs.app.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupShare implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int MAXTYPE_COUNT = 3;
	public static final int MESSAGE_TYPE_IMAGETEXT = 0x00;
	public static final int MESSAGE_TYPE_VOICETEXT = 0x01;
	public static final int MESSAGE_TYPE_VOTE = 0x02;

	// type is imagetext or voicetext or vote
	public int mType;

	public String gsid;
	public String type;// "imagetext" || "voicetext" || "vote"
	public String phone;
	public long time;
	public ArrayList<String> praiseusers = new ArrayList<String>();
	public ArrayList<Comment> comments = new ArrayList<Comment>();
	public ShareContent content = new ShareContent();

	public static class ShareContent implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// "imagetext" || "voicetext"
		public ArrayList<String> images = new ArrayList<String>();
		public ArrayList<VoiceContent> voices = new ArrayList<VoiceContent>();
		public String text = "";

		// "vote"
		public String title = "";
		public ArrayList<VoteContent> voteoptions = new ArrayList<VoteContent>();

		public void addImage(String imageName) {
			this.images.add(imageName);
		}

		public void addVoice(VoiceContent voiceContent) {
			this.voices.add(voiceContent);
		}

		@Override
		public boolean equals(Object o) {
			boolean flag = false;
			if (o != null) {
				try {
					ShareContent gs = (ShareContent) o;
					if (voteoptions.containsAll(gs.voteoptions)) {
						flag = true;
					}
				} catch (Exception e) {
					flag = false;
				}
			}
			return flag;
		}
	}

	public static class VoteContent implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String content = "";
		public ArrayList<String> voteUsers = new ArrayList<String>();

		public void addVoteUser(String phone) {
			voteUsers.add(phone);
		}

		@Override
		public boolean equals(Object o) {
			boolean flag = false;
			if (o != null) {
				try {
					VoteContent gs = (VoteContent) o;
					if (voteUsers.containsAll(gs.voteUsers)) {
						flag = true;
					}
				} catch (Exception e) {
					flag = false;
				}
			}
			return flag;
		}
	}

	public static class VoiceContent implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String fileName = "";
		public long time = 0;
	}

	@Override
	public boolean equals(Object o) {
		boolean flag = false;
		if (o != null) {
			try {
				GroupShare gs = (GroupShare) o;
				if (gsid.equals(gs.gsid) && type.equals(gs.type)
						&& phone.equals(gs.phone) && time == gs.time
						&& praiseusers.containsAll(gs.praiseusers)
						&& comments.containsAll(gs.comments)
						&& content.equals(gs.content)) {
					flag = true;
				}
			} catch (Exception e) {
				flag = false;
			}
		}
		return flag;
	}
}
