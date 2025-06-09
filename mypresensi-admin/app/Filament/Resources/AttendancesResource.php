<?php

namespace App\Filament\Resources;

use App\Filament\Resources\AttendancesResource\Pages;
use App\Models\Attendance;
use App\Models\User;
use App\Models\Location;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Forms\Components\Select;
use Filament\Forms\Components\TextInput;
use Filament\Forms\Components\TimePicker;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Table;
use Filament\Tables\Columns\TextColumn;
use Filament\Tables\Filters\SelectFilter;
use Carbon\Carbon;
use App\Filament\Resources\AttendanceResource\Components\AttachmentColumn;

class AttendancesResource extends Resource
{
    protected static ?string $model = Attendance::class;

    protected static ?string $navigationIcon = 'heroicon-o-check-circle';
    protected static ?string $navigationGroup = 'Kelola Absensi';
    protected static ?string $navigationLabel = 'Absensi';

    public static function form(Form $form): Form
    {
        return $form
            ->schema([
                Select::make('user_id')
                    ->label('Pengguna')
                    ->options(User::all()->pluck('name', 'id'))
                    ->searchable()
                    ->required(),

                Select::make('location_id')
                    ->label('Lokasi Kerja')
                    ->options(Location::all()->pluck('name', 'id'))
                    ->searchable()
                    ->required()
                    ->reactive()
                    ->afterStateUpdated(function (callable $set, $state) {
                        // Fetch the location name based on the selected location_id
                        $location = Location::find($state);
                        $set('location_name', $location ? $location->name : null);
                    }),

                TextInput::make('location_name')
                    ->label('Lokasi Absen')
                    ->required(),

                TextInput::make('latitude')
                    ->label('Latitude')
                    ->nullable(),

                TextInput::make('longitude')
                    ->label('Longitude')
                    ->nullable(),

                TimePicker::make('check_in_time')
                    ->label('Waktu Masuk')
                    ->nullable(),

                TimePicker::make('check_out_time')
                    ->label('Waktu Keluar')
                    ->nullable(),

                Select::make('type')
                    ->label('Jenis Absensi')
                    ->options([
                        'hadir' => 'Hadir',
                        'sakit' => 'Sakit',
                        'izin' => 'Izin',
                        'alpa' => 'Alpa',
                    ])
                    ->required()
                    ->reactive()
                    ->afterStateUpdated(function (callable $set, $state) {
                        if (!in_array($state, ['sakit', 'izin'])) {
                            $set('reason', null);
                        }
                    }),

                TextInput::make('reason')
                    ->label('Alasan')
                    ->nullable()
                    ->visible(fn($get) => in_array($get('type'), ['izin', 'sakit'])),
            ]);
    }

    public static function table(Table $table): Table
    {
        return $table
            ->columns([
                TextColumn::make('user.name')
                    ->label('Pengguna')
                    ->searchable(),

                TextColumn::make('location.name')
                    ->label('Lokasi Kerja'),

                TextColumn::make('location_name') // Display location_name in the table
                    ->label('Nama Lokasi'),

                TextColumn::make('device_name') // Display location_name in the table
                    ->label('Nama Perangkat'),

                TextColumn::make('type')
                    ->label('Jenis Absensi'),

                TextColumn::make('check_in_time')
                    ->label('Waktu Masuk')
                    ->time(),


                TextColumn::make('check_out_time')
                    ->label('Waktu Keluar')
                    ->time(),

                TextColumn::make('created_at')
                    ->label('Tanggal')
                    ->sortable('asc')
                    ->date(),

                TextColumn::make('status')
                    ->label('Status'),

                TextColumn::make('reason')
                    ->label('Alasan'),

                AttachmentColumn::make('attachment')
                ->label('Lampiran')


            ])
            ->defaultSort('created_at', 'desc')

            ->actions([
                Tables\Actions\EditAction::make(),
            ])
            ->bulkActions([
                Tables\Actions\DeleteBulkAction::make(),
            ]);
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListAttendances::route('/'),
            'create' => Pages\CreateAttendances::route('/create'),
            'edit' => Pages\EditAttendances::route('/{record}/edit'),
        ];
    }
}
