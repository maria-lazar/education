import {Photo} from "../photo/usePhotoGallery";
import {LocPosition} from "../location/MapPopover";

export interface PetProps {
    _id?: string;
    frontendId?: string;
    name: string;
    type?: string;
    breed?: string;
    birthDate?: number;
    description?: string;
    weight?: number;
    owner?: string;
    vaccinated?: boolean;
    ownerName?: string;
    saved?: boolean;
    lastModified?: number;
    version: number;
    conflict: boolean;
    photo?: Photo;
    location?: LocPosition;
}
