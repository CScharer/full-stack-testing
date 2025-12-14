-- ONE GOAL Database Schema (Corrected)
-- This schema fixes all issues identified in the AI review:
-- 1. Proper Foreign Key constraints
-- 2. Correct data types (INTEGER for Foreign Keys)
-- 3. Consistent naming
-- 4. Normalized structure

-- Enable Foreign Key support in SQLite
PRAGMA foreign_keys = ON;

-- Synchronization table (SQLite ↔ MongoDB mapping)
CREATE TABLE "application_sync" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "sqlite_id" INTEGER NOT NULL,
    "mongodb_id" TEXT,
    "created_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "modified_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "created_by" TEXT NOT NULL,
    "modified_by" TEXT NOT NULL,
    FOREIGN KEY("sqlite_id") REFERENCES "application"("id")
);

-- Core application table
CREATE TABLE "application" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "status" TEXT NOT NULL DEFAULT 'Pending',
    "requirement" TEXT,
    "work_setting" TEXT NOT NULL DEFAULT 'Remote',
    "compensation" TEXT,
    "position" TEXT,
    "job_description" TEXT,
    "resume" TEXT,
    "cover_letter" TEXT,
    "entered_iwd" INTEGER DEFAULT 0,
    "date_close" TEXT,
    "company_id" INTEGER,  -- Foreign Key to company table
    "created_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "modified_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "created_by" TEXT NOT NULL,
    "modified_by" TEXT NOT NULL,
    FOREIGN KEY("company_id") REFERENCES "company"("id")
);

-- Company/Firm information
CREATE TABLE "company" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "name" TEXT NOT NULL,  -- Firm name
    "address" TEXT,
    "city" TEXT,
    "state" TEXT,
    "zip" TEXT,
    "country" TEXT NOT NULL DEFAULT 'United States',
    "job_type" TEXT NOT NULL DEFAULT 'Technology',  -- Industry
    "created_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "modified_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "created_by" TEXT NOT NULL,
    "modified_by" TEXT NOT NULL
);

-- Contact information (recruiters, managers, leads)
CREATE TABLE "contact" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "company_id" INTEGER,  -- Optional: link to company
    "application_id" INTEGER,  -- Optional: link to specific application
    "name" TEXT NOT NULL,
    "title" TEXT NOT NULL DEFAULT 'Recruiter',
    "linkedin" TEXT,
    "contact_type" TEXT NOT NULL,  -- 'Recruiter', 'Manager', 'Lead', 'Account Manager'
    "created_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "modified_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "created_by" TEXT NOT NULL,
    "modified_by" TEXT NOT NULL,
    FOREIGN KEY("company_id") REFERENCES "company"("id"),
    FOREIGN KEY("application_id") REFERENCES "application"("id")
);

-- Contact emails (supports multiple emails per contact: Personal, Work, etc.)
CREATE TABLE "contact_email" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "contact_id" INTEGER NOT NULL,
    "email" TEXT NOT NULL,
    "email_type" TEXT NOT NULL DEFAULT 'Work',  -- 'Personal', 'Work', 'Other'
    "is_primary" INTEGER DEFAULT 0,  -- Boolean: 1 for primary email, 0 for others
    "created_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "modified_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "created_by" TEXT NOT NULL,
    "modified_by" TEXT NOT NULL,
    FOREIGN KEY("contact_id") REFERENCES "contact"("id") ON DELETE CASCADE
);

-- Contact phone numbers (supports multiple phones per contact: Home, Cell, Work, etc.)
CREATE TABLE "contact_phone" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "contact_id" INTEGER NOT NULL,
    "phone" TEXT NOT NULL,
    "phone_type" TEXT NOT NULL DEFAULT 'Work',  -- 'Home', 'Cell', 'Work', 'Other'
    "is_primary" INTEGER DEFAULT 0,  -- Boolean: 1 for primary phone, 0 for others
    "created_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "modified_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "created_by" TEXT NOT NULL,
    "modified_by" TEXT NOT NULL,
    FOREIGN KEY("contact_id") REFERENCES "contact"("id") ON DELETE CASCADE
);

-- Application notes
CREATE TABLE "note" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "application_id" INTEGER NOT NULL,
    "note" TEXT NOT NULL,
    "created_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "modified_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "created_by" TEXT NOT NULL,
    "modified_by" TEXT NOT NULL,
    FOREIGN KEY("application_id") REFERENCES "application"("id")
);

-- Job search sites (reference data)
CREATE TABLE "job_search_site" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
    "site_name" TEXT NOT NULL UNIQUE,
    "created_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "modified_on" TIMESTAMP NOT NULL DEFAULT (datetime('now', 'localtime')),
    "created_by" TEXT NOT NULL,
    "modified_by" TEXT NOT NULL
);

-- Create indexes on Foreign Keys for better performance
CREATE INDEX "idx_application_company_id" ON "application"("company_id");
CREATE INDEX "idx_contact_company_id" ON "contact"("company_id");
CREATE INDEX "idx_contact_application_id" ON "contact"("application_id");
CREATE INDEX "idx_note_application_id" ON "note"("application_id");
CREATE INDEX "idx_application_sync_sqlite_id" ON "application_sync"("sqlite_id");
CREATE INDEX "idx_contact_email_contact_id" ON "contact_email"("contact_id");
CREATE INDEX "idx_contact_email_primary" ON "contact_email"("contact_id", "is_primary");
CREATE INDEX "idx_contact_phone_contact_id" ON "contact_phone"("contact_id");
CREATE INDEX "idx_contact_phone_primary" ON "contact_phone"("contact_id", "is_primary");
