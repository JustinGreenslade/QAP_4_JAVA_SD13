-- Create table if not already created
CREATE TABLE IF NOT EXISTS patients (
    patient_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    condition VARCHAR(255)
);

-- Insert preloaded data if not already inserted
INSERT INTO patients (patient_id, first_name, last_name, age, condition) VALUES
  (1, 'Justin', 'Greenslade', 33, 'Asthma'),
  (2, 'Ashton', 'Dennis', 19, 'Diabetes'),
  (3, 'Joey', 'Thomas', 20, 'Hypertension'),
  (4, 'Joseph', 'Gallant', 35, 'Arthritis'),
  (5, 'Michael', 'Barney', 34, 'Amnesia')
ON CONFLICT (patient_id) DO NOTHING;

-- reset id to start counting from 6 for next added patient
SELECT setval(pg_get_serial_sequence('patients', 'patient_id'), 5);

