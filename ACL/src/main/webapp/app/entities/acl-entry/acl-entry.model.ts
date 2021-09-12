import { IAclSid } from 'app/entities/acl-sid/acl-sid.model';
import { IAclObjectIdentity } from 'app/entities/acl-object-identity/acl-object-identity.model';
import { IAclMask } from 'app/entities/acl-mask/acl-mask.model';

export interface IAclEntry {
  id?: number;
  granting?: boolean | null;
  aclSid?: IAclSid | null;
  aclObjectIdentity?: IAclObjectIdentity | null;
  mask?: IAclMask | null;
}

export class AclEntry implements IAclEntry {
  constructor(
    public id?: number,
    public granting?: boolean | null,
    public aclSid?: IAclSid | null,
    public aclObjectIdentity?: IAclObjectIdentity | null,
    public mask?: IAclMask | null
  ) {
    this.granting = this.granting ?? false;
  }
}

export function getAclEntryIdentifier(aclEntry: IAclEntry): number | undefined {
  return aclEntry.id;
}
