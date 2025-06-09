<?php
namespace App\Filament\Resources\AttendancesSummaryResource\Pages;

use App\Filament\Resources\AttendancesSummaryResource;
use Filament\Pages\Actions;
use Filament\Resources\Pages\EditRecord;

class EditAttendancesSummary extends EditRecord
{
    protected static string $resource = AttendancesSummaryResource::class;

    // Override the saved() method to call calculateSummary after saving the record
    protected function saved(): void
    {
        parent::saved(); // Ensure the parent saved method is still called

        // Get the form state (the updated data)
        $data = $this->getFormState();

        // Call the calculateSummary method after saving the record
        $this->record->calculateSummary($data['user_id'], $data['month'], $data['year']);
    }
}
