import { IAclEntry } from 'app/entities/acl-entry/acl-entry.model';
import { IAclClass } from 'app/entities/acl-class/acl-class.model';

export interface IAclObjectIdentity {
  id?: number;
  objectIdIdentity?: number;
  parentObject?: number;
  ownerSid?: number | null;
  entriesInheriting?: boolean | null;
  aclEntries?: IAclEntry[] | null;
  aclClass?: IAclClass | null;
}

export class AclObjectIdentity implements IAclObjectIdentity {
  constructor(
    public id?: number,
    public objectIdIdentity?: number,
    public parentObject?: number,
    public ownerSid?: number | null,
    public entriesInheriting?: boolean | null,
    public aclEntries?: IAclEntry[] | null,
    public aclClass?: IAclClass | null
  ) {
    this.entriesInheriting = this.entriesInheriting ?? false;
  }
}

export function getAclObjectIdentityIdentifier(aclObjectIdentity: IAclObjectIdentity): number | undefined {
  return aclObjectIdentity.id;
}
