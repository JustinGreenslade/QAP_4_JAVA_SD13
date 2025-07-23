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
  (1, 'Justin', 'Greenslade', 30, 'Asthma'),
  (2, 'Ashton', 'Dennis', 19, 'Diabetes'),
  (3, 'Joey', 'Thomas', 19, 'Hypertension'),
  (4, 'Joseph', 'Gallant', 30, 'Arthritis')
ON CONFLICT (patient_id) DO NOTHING;

-- reset id to start from 5 for next patient
SELECT setval(pg_get_serial_sequence('patients', 'patient_id'), 4);

