import { IAclSid } from 'app/entities/acl-sid/acl-sid.model';
import { IAclObjectIdentity } from 'app/entities/acl-object-identity/acl-object-identity.model';

export interface IAclEntry {
  id?: number;
  aceOrder?: number;
  mask?: number | null;
  granting?: boolean | null;
  auditSuccess?: boolean | null;
  auditFailure?: boolean | null;
  aclSid?: IAclSid | null;
  aclObjectIdentity?: IAclObjectIdentity | null;
}

export class AclEntry implements IAclEntry {
  constructor(
    public id?: number,
    public aceOrder?: number,
    public mask?: number | null,
    public granting?: boolean | null,
    public auditSuccess?: boolean | null,
    public auditFailure?: boolean | null,
    public aclSid?: IAclSid | null,
    public aclObjectIdentity?: IAclObjectIdentity | null
  ) {
    this.granting = this.granting ?? false;
    this.auditSuccess = this.auditSuccess ?? false;
    this.auditFailure = this.auditFailure ?? false;
  }
}

export function getAclEntryIdentifier(aclEntry: IAclEntry): number | undefined {
  return aclEntry.id;
}
