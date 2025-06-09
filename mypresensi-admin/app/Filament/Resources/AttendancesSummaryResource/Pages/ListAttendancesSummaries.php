<?php
namespace App\Filament\Resources\AttendancesSummaryResource\Pages;

use App\Filament\Resources\AttendancesSummaryResource;
use App\Exports\AttendancesSummaryExport;
use Filament\Pages\Actions;
use Filament\Resources\Pages\ListRecords;
use Filament\Tables\Actions\Action;
use Maatwebsite\Excel\Facades\Excel;
use Illuminate\Http\Request;

class ListAttendancesSummaries extends ListRecords
{
    protected static string $resource = AttendancesSummaryResource::class;

    // Customizing the header actions (like creating a new attendance summary)
    protected function getHeaderActions(): array
    {
        return [
            Actions\CreateAction::make(), // To create new record
            // Actions\Action::make('export')
            //     ->label('Ekspor ke Excel')
            //     ->action(function (Request $request) {
            //         // Get applied filters from URL
            //         $filters = $this->getFiltersFromRequest($request);

            //         return Excel::download(new AttendancesSummaryExport($filters), 'attendances_summary.xlsx');
            //     })
            //     ->icon('heroicon-o-arrow-down'), // Ensure this icon exists in the heroicons set
        ];
    }

    // protected function getFiltersFromRequest(Request $request): array
    // {
    //     $filters = [];

    //     if ($request->has('tableFilters.year.value')) {
    //         $filters['year'] = $request->input('tableFilters.year.value');
    //     }

    //     if ($request->has('tableFilters.month.value')) {
    //         $filters['month'] = $request->input('tableFilters.month.value');
    //     }

    //     return $filters;
    // }

    // Optionally, you can customize other elements, like table columns, filters, etc.
    protected function getTableActions(): array
    {
        return [
            // Define specific actions that should appear in the table
        ];
    }
}