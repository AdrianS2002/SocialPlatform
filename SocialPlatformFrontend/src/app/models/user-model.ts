export interface User {
  id: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  bio?: string;
  isBlocked?: boolean;
  isValidated?: boolean;
  profilePicture?: any
}
