export interface FeedPost {
  imageId: number;
  albumTitle: string;
  albumDescription: string;
  postedAt: string;
  postedByName: string;
  postedById: number;
  isFriend: boolean;
  base64Image?: string;
  isBlocked: boolean;
}
